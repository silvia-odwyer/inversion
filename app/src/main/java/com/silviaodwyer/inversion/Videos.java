package com.silviaodwyer.inversion;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Videos extends AppCompatActivity implements VideosRecyclerView.ItemClickListener {
  private Button uploadVideo;
  private static Integer RESULT_LOAD_VIDEO = 9;
  private MainApplication mainApplication;
  private FileUtils fileUtils;
  private String videoPath;
  private String videoUrl;
  private Button deleteAllVideosBtn;
  private Video video;
  private int numVideos;
  private VideosRecyclerView recyclerViewAdapter;
  private ArrayList<VideoMetadata> savedVideoMetadata;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_videos);

    mainApplication = ((MainApplication)getApplication());
    fileUtils = new FileUtils(getApplicationContext());
    savedVideoMetadata = mainApplication.getSavedVideoMetadata(getApplicationContext());
    numVideos = savedVideoMetadata.size();
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

    deleteAllVideosBtn = findViewById(R.id.delete_videos_btn);
    deleteAllVideosBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteAllVideos();
      }
    });
//    mainApplication.requestPermissions(Videos.this);
  }

  private void setUpRecyclerView() {
    // set up the RecyclerView
    RecyclerView recyclerView = findViewById(R.id.videos_recycler_view);
    int noOfColumns = 3;
    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), noOfColumns));
    recyclerViewAdapter = new VideosRecyclerView(getApplicationContext(), savedVideoMetadata, mainApplication);
    recyclerViewAdapter.setClickListener(this);
    recyclerView.setAdapter(recyclerViewAdapter);
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d("DEBUG", "RESUMED IMAGES ACTIVITY");
    ArrayList<VideoMetadata> savedVideoMetadata = mainApplication.getSavedVideoMetadata(getApplicationContext());

    if (savedVideoMetadata.size() != numVideos) {
        recyclerViewAdapter.updateRecyclerView(savedVideoMetadata);
    }
  }

  @Override
  protected void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      Uri videoUri = data.getData();

      // Convert the video URI to a path
      videoUrl = uriToPath(videoUri);

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

  @Override
  public void onItemClick(View view, int position) {

  }

  public void thumbnailVideo(final String videoPath) throws ExecutionException, InterruptedException {
    // thumbnail the video in a background thread
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
          .load(videoUrl)
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
      Bitmap resizedBitmap = imageUtils.resizeBitmap(result, 200, 200);
      Intent intent = new Intent(getBaseContext(), VideoEditor.class);

      if (videoUrl != null) {
        video = new Video(resizedBitmap, videoUrl);

        mainApplication.setVideo(video);
        intent.putExtra("videoUrl", videoUrl);

      }

      // Now that we have the bitmap thumbnail of the video, we can start the video editor activity.
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      getApplication().startActivity(intent);
      super.onPostExecute(result);
    }
  }

  public void deleteAllVideos() {
    File directory = new File(Environment.getExternalStorageDirectory() + "/Inversion/videos");

    fileUtils.deleteAllFilesInDirectory(directory);
    mainApplication.deleteAllMetadata(FileMetadata.FileType.VIDEO);

    // delete all thumbnails
    File vidThumbnailsDirectory = new File(Environment.getExternalStorageDirectory() + "/Inversion/videos/thumbnails");

    fileUtils.deleteAllFilesInDirectory(vidThumbnailsDirectory);
    this.printSavedVideos();
  }

  private void printSavedVideos() {

    if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
      File inversionDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos");

      ArrayList<String> filepaths = new ArrayList<String>();

      File files[] = inversionDirectory.listFiles();

      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          filepaths.add(files[i].getAbsolutePath());
          Log.d("DEBUG", "EXTERNAL DIR IMAGE: " + files[i].getAbsolutePath());
        }
      }
      Log.d("DEBUG", "Filepaths: " + filepaths.toString());
    }
  }
}
