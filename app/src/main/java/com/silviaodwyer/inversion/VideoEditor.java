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
import android.view.ViewGroup;
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
  private EPlayerView ePlayerView;
  private ImageView imageView;
  private String videoURL = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";
  private String imageURL = "https://images.unsplash.com/photo-1567359781514-3b964e2b04d6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDkwNH0&fm=png&w=100";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_editor);
    BottomNavigationView navView = findViewById(R.id.nav_view);

    videoPath = getIntent().getExtras().getString("videoPath");
    videoURL = videoPath;

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    NavigationUI.setupWithNavController(navView, navController);

    mainApplication = ((MainApplication)getApplication());

    mainApplication.requestPermissions(VideoEditor.this);


    setupVideo();
  }

  private void setupVideo() {

    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(),"Inversion"));

    MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
      .createMediaSource(Uri.parse(videoURL));

    SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this);

    player.prepare(videoSource);
    player.setPlayWhenReady(true);
    ePlayerView = new EPlayerView(this);

    ePlayerView.setSimpleExoPlayer(player);
    ePlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    LinearLayout linLayout =  findViewById(R.id.video_container);
    linLayout.addView(ePlayerView);
    ePlayerView.onResume();
    filterPlayerView(new GlSepiaFilter());

    try {
      thumbnailVideo(videoURL);
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
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
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/images");
            directory.mkdirs();

            File outputFile = new File(directory.toString(), "video.mp4");
            fileOutputStream = new FileOutputStream(outputFile);

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

  public void thumbnailVideo(final String videoPath) throws ExecutionException, InterruptedException {
    new Thread(new Runnable() {
      @Override
      public void run() {
        new BitmapAsyncTask().execute();
      }
    }).start();

  }


  class BitmapAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

    protected void onPreExecute() {
      super.onPreExecute();
    }

    protected Bitmap doInBackground(Void...arg0) {
      Bitmap bitmap = null;
      FutureTarget<Bitmap> futureTarget =
        Glide.with(getApplicationContext())
          .asBitmap()
          .load(videoPath)
          .submit(50, 50);

      try {
        bitmap = futureTarget.get();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return bitmap;
    }

    protected void onProgressUpdate(Integer...a) {
      super.onProgressUpdate(a);
    }

    protected void onPostExecute(Bitmap result) {
      ImageView imageView2 = findViewById(R.id.vidthumbnail);
      imageView2.setImageBitmap(result);
      super.onPostExecute(result);
    }
  }

  private void filterPlayerView(GlFilter filter) {
    ePlayerView.setGlFilter(filter);
  }

}
