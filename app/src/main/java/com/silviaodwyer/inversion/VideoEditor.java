package com.silviaodwyer.inversion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
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
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.silviaodwyer.inversion.utils.VideoFilters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
  private ArrayList<VideoThumbnail> gradientGrayscaleThumbnails = new ArrayList<>();
  private ArrayList<VideoThumbnail> gradientThumbnails = new ArrayList<>();
  private ArrayList<VideoThumbnail> vintageThumbnails = new ArrayList<>();
  private ArrayList<VideoThumbnail> dissolveThumbnails = new ArrayList<>();
  private ArrayList<VideoThumbnail> colorBlendThumbnails = new ArrayList<>();
  private GlFilter activeFilter = new GlSepiaFilter();
  private String videoURL = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";
  private String imageURL = "https://images.unsplash.com/photo-1567359781514-3b964e2b04d6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDkwNH0&fm=png&w=100";
  private LinearLayout navOverlay;
  private HorizontalScrollView bottomNavBar;
  private Bitmap originalVideoThumbnail;
  private VideoFilters videoFilters;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_editor);

    context = getApplicationContext();
    mainApplication = ((MainApplication)getApplication());
    videoFilters = new VideoFilters(context);

    video = mainApplication.getVideo();
    Log.d("DEBUG", "VIDEO NAME IS: " + video.getMetadata().getName());

    if (player != null) {
        player = null;
        ePlayerView = null;
        dataSourceFactory = null;
    }
    navBtnNames = new String[]{"filters", "gradient_effects"};
    navOverlay = findViewById(R.id.nav_overlay_ve);
    bottomNavBar = findViewById(R.id.video_editor_bottom_menu);
    navEffectCategories = findViewById(R.id.video_editor_fx_categories);
    originalVideoThumbnail = video.getThumbnail();

    videoUrl = video.getMetadata().getOriginalVideoPath();
    Log.d("DEBUG", "Video Path is: " + videoUrl);

    this.setupPlayer();
    this.initThumbnailsRecyclerView();

    ImageButton btn = findViewById(R.id.saveVideoBtn);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openDownloadActivity();
      }
    });

    initNavBar();
  }

  public void initNavBar() {

    for (int index = 0; index < navBtnNames.length; index++) {
      int id = getResources().getIdentifier("ve_btn_" + navBtnNames[index], "id",
              getPackageName());
      LinearLayout ie_btn =  findViewById(id);
      ie_btn.setOnClickListener(onClickListener);
    }

  }

  private void initThumbnailsRecyclerView() {
    thumbnailsRecyclerView = findViewById(R.id.filtered_video_thumbnails);
    LinearLayoutManager layoutManager
            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
    thumbnailsRecyclerView.setLayoutManager(layoutManager);

    List<List<Object>> filters = videoFilters.createEffectFilters();

    vintageThumbnails = videoFilters.getFilteredThumbnails(originalVideoThumbnail, filters);
    adapter = new VideoThumbnailsRecyclerView(VideoEditor.this, vintageThumbnails, mainApplication);

    thumbnailsRecyclerView.setAdapter(adapter);

  }

  ImageButton.OnClickListener onClickListener = new ImageButton.OnClickListener() {
    @Override
    public void onClick(View view) {
      thumbnailsRecyclerView.setVisibility(View.VISIBLE);
      navOverlay.setVisibility(View.VISIBLE);

      for (int index = 0; index < navBtnNames.length; index++) {
        int id = getResources().getIdentifier("ve_btn_" + navBtnNames[index], "id", getPackageName());
        LinearLayout btn_layout = findViewById(id);
        btn_layout.setBackgroundColor(Color.TRANSPARENT);
      }
      view.setBackgroundColor(Color.BLACK);
      String nav_btn_tag = view.getTag().toString();
      bottomNavBar.setVisibility(GONE);
      navEffectCategories.removeAllViews();

      switch (nav_btn_tag) {
        case "filters": {
          String[] gradientEffectNavNames = {"Gradients", "Vintage"};
          createCategoryMenu(gradientEffectNavNames);

//              LinearLayout overlay = findViewById(R.id.nav_overlay);
//              overlay.setVisibility(View.VISIBLE);

          break;
        }
        case "gradient_effects": {
          Log.d("DEBUG", "GRADIENT EFFECTS CLICKED");

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
                    if (gradientThumbnails.size() == 0) {
                        gradientThumbnails.addAll(videoFilters.getFilteredThumbnails(originalVideoThumbnail,
                                videoFilters.createVintageFilters()));
                    }
                    adapter.update(gradientThumbnails);

                    break;
                }
                case "vintage": {
                    if (vintageThumbnails.size() == 0) {
                        vintageThumbnails.addAll(videoFilters.getFilteredThumbnails(originalVideoThumbnail,
                                videoFilters.createVintageFilters()));
                    }
                    adapter.update(vintageThumbnails);
                    break;
                }
                case "dissolve": {
                    break;
                }
                case "color blend": {

                    break;
                }
                case "neon": {
                    adapter.clear();

//                    new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            if (gradientGrayscaleThumbnails.size() == 0) {
//                                gradientGrayscaleThumbnails.addAll(gradientFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
//                                        gradientFilters.createGradientGrayscaleFilters()));
//                            }
//
//                            Message msg = new Message();
//                            msg.obj = gradientGrayscaleThumbnails;
//                            handler.sendMessage(msg);
//                        }
//                    }).start();

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
    startActivity(intent);
  }
}
