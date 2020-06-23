package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.concurrent.ExecutionException;

public class Videos extends AppCompatActivity implements VideosRecyclerView.ItemClickListener {
  private Button uploadVideo;
  private static Integer RESULT_LOAD_VIDEO = 9;
  private MainApplication mainApplication;
  private FileUtils fileUtils;
  private String videoPath;
  private VideoPlayer videoPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_videos);

    mainApplication = ((MainApplication)getApplication());
    fileUtils = new FileUtils(getApplicationContext());

    setUpRecyclerView();

    uploadVideo = findViewById(R.id.upload_video_btn);
    uploadVideo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent videoPickerIntent = new Intent(Intent.ACTION_PICK);
        videoPickerIntent.setType("video/*");
        startActivityForResult(videoPickerIntent, RESULT_LOAD_VIDEO);
      }
    });
//    mainApplication.requestPermissions(Videos.this);
  }

  private void setUpRecyclerView() {
    // set up the RecyclerView
    ArrayList<Bitmap> data = new ArrayList<>();
    RecyclerView recyclerView = findViewById(R.id.videos_recycler_view);
    int noOfColumns = 3;
    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), noOfColumns));
    VideosRecyclerView recyclerViewAdapter = new VideosRecyclerView(getApplicationContext(), data, mainApplication);
    recyclerViewAdapter.setClickListener(this);
    recyclerView.setAdapter(recyclerViewAdapter);
  }

  @Override
  protected void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      final String videoUrl = data.getData().getPath();
      Uri videoUri = data.getData();

      // Convert the video URI to a path
      videoPath = uriToPath(videoUri);

      // Set up video player before starting async task
      videoPlayer = new VideoPlayer(getApplicationContext(), videoPath);

      try {
        thumbnailVideo(videoPath);
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    else {
      // user has not chosen an image, display a Toast message
      Toast.makeText(this, "You haven't chosen a video.", Toast.LENGTH_LONG).show();
    }
  }

  public String uriToPath(Uri uri) {
    String[] projection = {MediaStore.Video.Media.DATA};

    try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
      if (cursor != null) {
        int columnIndex = cursor
          .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
      } else
        return null;
    }
  }

  public void copyVideo(String videoPath) {
    File dst = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos");
    dst.mkdirs();
    File outputFile = new File(dst.getPath() + File.separator + "copied_video.mp4");
    File src = new File(videoPath);
    fileUtils.copyFile(src, outputFile);
    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));

  }

  @Override
  public void onItemClick(View view, int position) {

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
          .submit(300, 300);

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
      ImageUtils imageUtils = new ImageUtils(getApplicationContext());
      Bitmap resizedBitmap = imageUtils.resizeBitmap(result, 250, 250);
      Video video = new Video(resizedBitmap);
      mainApplication.setVideo(video);
      mainApplication.setVideoPlayer(videoPlayer);
      Intent intent = new Intent(getBaseContext(), VideoEditor.class);

      if (videoPath != null) {
        intent.putExtra("videoPath", videoPath);
      }

      // Now that we have the bitmap thumbnail of the video, we can start the video editor activity.
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      getApplication().startActivity(intent);
      super.onPostExecute(result);
    }
  }

}
