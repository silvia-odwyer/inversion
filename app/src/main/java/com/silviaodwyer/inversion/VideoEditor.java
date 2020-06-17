package com.silviaodwyer.inversion;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.io.FileOutputStream;
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
    mainApplication.requestPermissions(VideoEditor.this);
  }

  private void setupVideo() {
    String MP4_URL = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";


    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(),"Inversion"));

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


  @Override
  public void onRequestPermissionsResult(int reqCode,
                                         String permissions[], int[] grantResults) {
    switch (reqCode) {
      case 1: {
        if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          FileOutputStream fileOutputStream = null;
          try {
            Log.d("DEBUG", "Downloading video");
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion");
            directory.mkdirs();

            File outputFile = new File(directory.toString(), "video1.mp4");

            fileOutputStream = new FileOutputStream(outputFile);

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://www.radiantmediaplayer.com/media/bbb-360p.mp4"))
              .setTitle(outputFile.getName())
              .setDescription("Downloading")
              .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
              .setDestinationUri(Uri.fromFile(outputFile))
              .setAllowedOverMetered(true)
              .setAllowedOverRoaming(true);
            DownloadManager downloadManager = (DownloadManager) getApplication().getSystemService(getApplication().DOWNLOAD_SERVICE);
//            long downloadId = downloadManager.enqueue(request);
          }
          catch (IOException e) {
            Log.d("DEBUG", "Could not write file!" + e);
          }
          finally {
            try {
              fileOutputStream.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
            Uri.parse("file://" + "image.JPG")));

          Log.d("DEBUG", "SAVED IMAGE");


        } else {
          // permission was denied
        }
        return;
      }
    }
  }

}
