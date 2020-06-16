package com.silviaodwyer.inversion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daasuu.epf.EPlayerView;
import com.daasuu.epf.filter.GlSepiaFilter;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class VideoEditor extends AppCompatActivity {
  private MainApplication mainApplication;
  private String videoPath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_editor);
    BottomNavigationView navView = findViewById(R.id.nav_view);

    videoPath = getIntent().getExtras().getString("videoPath");

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    NavigationUI.setupWithNavController(navView, navController);

    mainApplication = ((MainApplication)getApplication());

    setupVideo();
  }

  private void setupVideo() {
    String MP4_URL = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";
    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(this, getApplicationInfo().name));

    MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
      .createMediaSource(Uri.parse(MP4_URL));

    SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this);

    player.prepare(videoSource);
    player.setPlayWhenReady(true);
    EPlayerView ePlayerView = new EPlayerView(this);

    ePlayerView.setSimpleExoPlayer(player);
    ePlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    LinearLayout linLayout =  findViewById(R.id.video_container);
    linLayout.addView(ePlayerView);
    ePlayerView.onResume();
    ePlayerView.setGlFilter(new GlSepiaFilter());

  }

}
