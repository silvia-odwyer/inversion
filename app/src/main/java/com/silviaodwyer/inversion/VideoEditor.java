package com.silviaodwyer.inversion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

public class VideoEditor extends AppCompatActivity {
  private MainApplication mainApplication;
  private boolean playVideoWhenForegrounded;
  private long lastPosition;
  private RecyclerView recyclerView;
  private ImageThumbnailsRecyclerView adapter;
  private GPUPlayerView ePlayerView;
  private SimpleExoPlayer player;
  private DataSource.Factory dataSourceFactory;
  private Context context;
  private String videoUrl;
  private Video video;
  private ImageView imageView;
  private GlFilter activeFilter = new GlSepiaFilter();
  private String videoURL = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";
  private String imageURL = "https://images.unsplash.com/photo-1567359781514-3b964e2b04d6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDkwNH0&fm=png&w=100";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_editor);

    context = getApplicationContext();
    mainApplication = ((MainApplication)getApplication());

    video = mainApplication.getVideo();
    Log.d("DEBUG", "VIDEO NAME IS: " + video.getMetadata().getName());

    if (player != null) {
        player = null;
        ePlayerView = null;
        dataSourceFactory = null;
    }

    videoUrl = video.getMetadata().getOriginalVideoPath();
    Log.d("DEBUG", "Video Path is: " + videoUrl);

    this.setupPlayer();
    this.setUpNavController();

    ImageButton btn = findViewById(R.id.saveVideoBtn);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openDownloadActivity();
      }
    });
  }

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
