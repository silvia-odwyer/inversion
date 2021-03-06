package com.silviaodwyer.inversion.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.egl.filter.GlSepiaFilter;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.daasuu.gpuv.player.PlayerScaleType;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.models.Video;
import com.silviaodwyer.inversion.video_filters.VideoFilterPacks;
import com.silviaodwyer.inversion.adapters.VideoThumbnailsRecyclerView;
import com.silviaodwyer.inversion.video_filters.VideoFilters;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static android.view.View.generateViewId;

public class VideoEditor extends AppCompatActivity {
  private MainApplication mainApplication;
  private boolean playVideoWhenForegrounded;
  private long lastPosition;
  private VideoThumbnailsRecyclerView adapter;
  private GPUPlayerView ePlayerView;
  private SimpleExoPlayer player;
  private DataSource.Factory dataSourceFactory;
  private Context context;
  private String videoUrl;
  private Video video;
  private String[] navBtnNames;
  private LinearLayout navEffectCategories;
  private RecyclerView thumbnailsRecyclerView;
  private SharedPreferences sharedPreferences;
  private GlFilter activeFilter = new GlSepiaFilter();
  private LinearLayout navOverlay;
  private HorizontalScrollView bottomNavBar;
  private Bitmap originalVideoThumbnail;
  private VideoFilters videoFilters;
  private String activeFilterName;
  private boolean isDarkTheme;
  private VideoFilterPacks videoFilterPacks;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_editor);

    context = getApplicationContext();
    mainApplication = ((MainApplication)getApplication());
    videoFilters = new VideoFilters(context);
    videoFilterPacks = new VideoFilterPacks(getApplicationContext());
    sharedPreferences = getApplicationContext().getSharedPreferences("PREF", Context.MODE_PRIVATE);

    isDarkTheme = sharedPreferences.getBoolean("nightThemeEnabled", isDarkTheme);

    video = mainApplication.getVideo();
    Log.d("DEBUG", "VIDEO NAME IS: " + video.getMetadata().getName());

    if (player != null) {
        player = null;
        ePlayerView = null;
        dataSourceFactory = null;
    }
    navBtnNames = new String[]{"filters", "gradient_effects", "glitch", "effects"};
    navOverlay = findViewById(R.id.nav_overlay_ve);
    bottomNavBar = findViewById(R.id.video_editor_bottom_menu);
    navEffectCategories = findViewById(R.id.video_editor_fx_categories);
    originalVideoThumbnail = video.getThumbnail();

    videoUrl = video.getMetadata().getOriginalVideoPath();
    Log.d("DEBUG", "Video Path is: " + videoUrl);

    this.setupPlayer();
    this.initThumbnailsRecyclerView();
    this.initClickListeners();
    initNavBar();
    initIcons();
  }

  // change color of icons depending on light/dark theme enabled
  public void initIcons() {
    HashMap<String, Integer> iconNamesToRes = new HashMap<>();
    iconNamesToRes.put("filters", R.drawable.ic_compare_black_24dp);
    iconNamesToRes.put("glitch", R.drawable.ic_texture_black_36dp);
    iconNamesToRes.put("correct", R.drawable.ic_photo_filter_black_36dp);
    iconNamesToRes.put("effects", R.drawable.ic_photo_filter_black_36dp);
    iconNamesToRes.put("gradient_effects", R.drawable.ic_color_lens_black_36dp);

    // icons are light theme by default given app's theme is dark theme is default
    if (!isDarkTheme) {
      ImageButton saveBtn = findViewById(R.id.save_btn);
      saveBtn.setImageResource(R.drawable.save_btn_black_24dp);

      ImageButton backBtn = findViewById(R.id.back_btn);
      backBtn.setImageResource(R.drawable.arrow_back_black_24dp);

      for (String navBtnName : navBtnNames ) {
        int id = getResources().getIdentifier("ie_icon_" + navBtnName, "id", getPackageName());
        ImageView imageView = findViewById(id);
        imageView.setImageResource(iconNamesToRes.get(navBtnName));
      }
    }

  }

  public void initClickListeners() {
    ImageButton btn = findViewById(R.id.save_video_btn);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openDownloadActivity();
      }
    });

    ImageButton backBtn = findViewById(R.id.back_btn_videeditor);
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // finish activity
        finish();
      }
    });
  }

  public void initNavBar() {

      for (String navBtnName : navBtnNames) {
          int id = getResources().getIdentifier("ve_btn_" + navBtnName, "id",
                  getPackageName());
          LinearLayout ie_btn = findViewById(id);
          ie_btn.setOnClickListener(onClickListener);
      }

  }

  private void initThumbnailsRecyclerView() {
    thumbnailsRecyclerView = findViewById(R.id.filtered_video_thumbnails);
    LinearLayoutManager layoutManager
            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
    thumbnailsRecyclerView.setLayoutManager(layoutManager);

    adapter = new VideoThumbnailsRecyclerView(VideoEditor.this, videoFilterPacks.getVintageFiltersMetadata(), mainApplication, ePlayerView);

    thumbnailsRecyclerView.setAdapter(adapter);

  }

  ImageButton.OnClickListener onClickListener = new ImageButton.OnClickListener() {
    @Override
    public void onClick(View view) {
      Log.d("DEBUG", "CLICKED");
      thumbnailsRecyclerView.setVisibility(View.VISIBLE);
      navOverlay.setVisibility(View.VISIBLE);

        for (String navBtnName : navBtnNames) {
            int id = getResources().getIdentifier("ve_btn_" + navBtnName, "id", getPackageName());
            LinearLayout btn_layout = findViewById(id);
            btn_layout.setBackgroundColor(Color.TRANSPARENT);
        }
      view.setBackgroundColor(Color.BLACK);
      String nav_btn_tag = view.getTag().toString();
      bottomNavBar.setVisibility(GONE);
      navEffectCategories.removeAllViews();


      switch (nav_btn_tag) {
        case "filters": {
          String[] gradientEffectNavNames = {"Popular", "Vintage"};
          createCategoryMenu(gradientEffectNavNames);

          break;
        }
        case "glitch": {
          String[] glitchEffectNavNames = {"Glitch", "Color Glitch"};
          createCategoryMenu(glitchEffectNavNames);

          break;
        }
        case "effects": {
          String[] effectNavNames = {"Warm", "Cold"};
          createCategoryMenu(effectNavNames);

          break;
        }
        case "gradient_effects": {
          String[] gradientEffectNavNames = {"Gradients", "Neon", "Dissolve", "Color Blend"};
          createCategoryMenu(gradientEffectNavNames);
          break;
        }

      }
      thumbnailsRecyclerView.scheduleLayoutAnimation();
    }
  };

  public void createCategoryMenu(String[] categoryNames) {

    for (String navName: categoryNames) {
      TextView textView = new TextView(getApplicationContext());
      navName = navName.toUpperCase();
      textView.setText(navName);
      textView.setId(generateViewId());
      textView.setOnClickListener(filterBtnOlickListener);
      textView.setTag(navName.toLowerCase());
      textView.setTextColor(Color.GRAY);
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
      );
      lp.setMargins(14, 12, 14, 14);
      textView.setLayoutParams(lp);
      navEffectCategories.addView(textView);
    }
  }

  TextView.OnClickListener filterBtnOlickListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            bottomNavBar.setVisibility(GONE);
            for (int index = 0; index < navEffectCategories.getChildCount(); index++) {
                TextView btn = (TextView) navEffectCategories.getChildAt(index);
                btn.setTextColor(Color.GRAY);
            }

            TextView btn = findViewById(view.getId());
            btn.setTextColor(Color.WHITE);

            String nav_btn_tag = view.getTag().toString();

            switch (nav_btn_tag) {
                case "gradients": {
                    adapter.update(videoFilterPacks.getGradientFiltersMetadata());
                    break;
                }
                case "vintage": {
                    adapter.update(videoFilterPacks.getVintageFiltersMetadata());
                    break;
                }
                case "dissolve": {
                  adapter.update(videoFilterPacks.getDissolveFiltersMetadata());
                    break;
                }
                case "glitch": {
                  adapter.update(videoFilterPacks.getGlitchFiltersMetadata());
                  break;
                }
                case "color glitch": {
                  adapter.update(videoFilterPacks.getColorGlitchFiltersMetadata());
                  break;
                }
                case "color blend": {
                adapter.update(videoFilterPacks.getColorBlendFiltersMetadata());
                break;
              }
              case "warm": {
                adapter.update(videoFilterPacks.getWarmTintFiltersMetadata());
                break;
              }
              case "cold": {
                adapter.update(videoFilterPacks.getColdTintFiltersMetadata());
                break;
              }
              case "popular": {
                adapter.update(videoFilterPacks.getPopularFiltersMetadata());
                break;
              }
                case "neon": {
                    adapter.clear();
                    break;
                }
            }
            thumbnailsRecyclerView.scheduleLayoutAnimation();
        }
    };
  public void setupPlayer() {
    player = ExoPlayerFactory.newSimpleInstance(context);
    player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

    ePlayerView = new GPUPlayerView(context);

    PlayerControlView controls = findViewById(R.id.video_player_controls);
    controls.setPlayer(player);

    // resize video view
    ePlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_FIT_WIDTH);
    ePlayerView.setSimpleExoPlayer(player);
    Uri uri = Uri.parse(videoUrl);

    ePlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context,"Inversion"));

    MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
      .createMediaSource(uri);

    player.prepare(videoSource);
    player.setPlayWhenReady(true);

    mainApplication.setPlayerView(ePlayerView);

    LinearLayout videoContainer = findViewById(R.id.video_container);
    videoContainer.addView(ePlayerView);

    ePlayerView.onResume();
  }

  public void end() {
      if (player != null) {
          player.stop();
          player.setPlayWhenReady(false);

          // Set player to null to avoid overuse of memory
          player.release();
          player = null;
      }
  }

  public void setUpNavController() {
    BottomNavigationView navView = findViewById(R.id.nav_view);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    NavigationUI.setupWithNavController(navView, navController);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {

      if (ePlayerView != null) {

      }
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    Log.d("DEBUG", "STOPPED VID EDITOR ACTIVITY");
//    playVideoWhenForegrounded = player.getPlayWhenReady();
//
//    lastPosition = player.getCurrentPosition();
    end();

  }

  @Override
  protected void onPause() {
    super.onPause();
    if (player != null) {
      player.stop();
      Log.d("DEBUG", "VID EDITOR ACTIVITY PAUSED");

//      lastPosition = player.getCurrentPosition();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d("DEBUG", "VID EDITOR ACTIVITY RESUMED");

    // TODO resume to last position in the player.
    if(lastPosition != 0  && player != null){
      player.seekTo(lastPosition);
    }
  }

  @Override
  public void onRequestPermissionsResult(int reqCode,
                                         String permissions[], int[] grantResults) {
    switch (reqCode) {
      case 1: {
        if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          openDownloadActivity();
        } else {
          // permission was denied
        }
        return;
      }
    }
  }

  private void stopPlayer() {
    // reset player if it already exists
    if (player != null) {
      Log.d("DEBUG", "PLAYER NOT NULL");
      player.setPlayWhenReady(false);
    }
  }

  private void releasePlayer() {
    stopPlayer();

    if (player != null) {
      Log.d("DEBUG", "PLAYER NOT NULL");

      player.release();
        player = null;
      }
  }

  public void openDownloadActivity() {
    Intent intent = new Intent(VideoEditor.this, DownloadProgress.class);
    intent.putExtra("videoPath", videoUrl);
    intent.putExtra("videoFilterName", activeFilterName);
    startActivity(intent);
  }

  public void setActiveVideoFilterName(String filterName) {
    this.activeFilterName = filterName;
  }

  @Override
  public void onBackPressed() {
    if (bottomNavBar.getVisibility() == View.VISIBLE) {
      super.onBackPressed();
    } else {
      navOverlay.setVisibility(GONE);
      thumbnailsRecyclerView.setVisibility(GONE);
      bottomNavBar.setVisibility(View.VISIBLE);
    }
  }
}
