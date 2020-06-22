package com.silviaodwyer.inversion;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.daasuu.epf.EPlayerView;
import com.daasuu.epf.filter.GlFilter;
import com.daasuu.epf.filter.GlLuminanceFilter;
import com.daasuu.epf.filter.GlSepiaFilter;
import com.daasuu.mp4compose.FillMode;
import com.daasuu.mp4compose.Rotation;
import com.daasuu.mp4compose.composer.Mp4Composer;
import com.daasuu.mp4compose.filter.GlFilterGroup;
import com.daasuu.mp4compose.filter.GlMonochromeFilter;
import com.daasuu.mp4compose.filter.GlVignetteFilter;
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
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.bumptech.glide.load.resource.UnitTransformation.get;

public class VideoEditor extends AppCompatActivity {
  private MainApplication mainApplication;
  private String videoPath;
  private SimpleExoPlayer player;
  private boolean playVideoWhenForegrounded;
  private long lastPosition;
  private VideoPlayer videoPlayer;
  private EPlayerView ePlayerView;
  private ImageView imageView;
  private DataSource.Factory dataSourceFactory;
  private String videoURL = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";
  private String imageURL = "https://images.unsplash.com/photo-1567359781514-3b964e2b04d6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDkwNH0&fm=png&w=100";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_editor);

    videoPath = getIntent().getExtras().getString("videoPath");

    mainApplication = ((MainApplication)getApplication());

    setupVideo();

    this.setUpNavController();

    ImageButton btn = findViewById(R.id.saveVideoBtn);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        mainApplication.requestPermissions(VideoEditor.this);
      }
    });

  }

  public void setUpNavController() {
    BottomNavigationView navView = findViewById(R.id.nav_view);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    NavigationUI.setupWithNavController(navView, navController);
  }

  public void setupVideo() {
    videoPlayer = mainApplication.getVideoPlayer();
    ePlayerView = videoPlayer.getPlayerView();
    player = videoPlayer.getSimpleExoPlayer();
    LinearLayout linLayout =  findViewById(R.id.video_container);
    linLayout.addView(ePlayerView);
  }

  @Override
  public void onStop() {

    super.onStop();
    Log.d("DEBUG", "STOPPED VID EDITOR ACTIVITY");
//    playVideoWhenForegrounded = player.getPlayWhenReady();
//
//    lastPosition = player.getCurrentPosition();
    player.stop();
    player.setPlayWhenReady(false);

    // Set player to null to avoid overuse of memory
//    player.release();

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
          saveVideo();

////          FileOutputStream fileOutputStream = null;
////          try {
////            Log.d("DEBUG", "Downloading video");
////            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/images");
////            directory.mkdirs();
////
////            File outputFile = new File(directory.toString(), "video.mp4");
////            fileOutputStream = new FileOutputStream(outputFile);
////
////          }
////          catch (IOException e) {
////            Log.d("DEBUG", "Could not write file!" + e);
////          }
////          finally {
////            try {
////              fileOutputStream.close();
////            } catch (IOException e) {
////              e.printStackTrace();
////            }
////          }
////          sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
////            Uri.parse("file://" + "image.JPG")));
////
////          Log.d("DEBUG", "SAVED IMAGE");
//
//
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

  private void saveVideo() {
    File dst = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos");
    dst.mkdirs();
    File outputFile = new File(dst.getPath() + File.separator + "saved_video.mp4");
    final String destMp4Path = outputFile.getPath();

    new Mp4Composer(videoPath, destMp4Path)
      .size( 540,  960)
      .fillMode(FillMode.PRESERVE_ASPECT_FIT)
      .filter(new GlFilterGroup(new GlVignetteFilter()))
      .listener(new Mp4Composer.Listener() {
        @Override
        public void onProgress(double progress) {
          Log.d("DEBUG", "Progress:  " + progress);
        }
        // TODO Update a progress bar to display progress.
        @Override
        public void onCompleted() {
          Log.d("DEBUG", "onCompleted()");
            Toast.makeText(getApplicationContext(), "codec complete path =" + destMp4Path, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
          Log.d("DEBUG", "onCanceled");
        }

        @Override
        public void onFailed(Exception exception) {
          Log.e("DEBUG", "onFailed()", exception);
          Toast.makeText(getApplicationContext(), "Error when saving video.", Toast.LENGTH_SHORT).show();
        }
      })
      .start();
  }

}
