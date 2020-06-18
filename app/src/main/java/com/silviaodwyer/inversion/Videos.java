package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.BitSet;

public class Videos extends AppCompatActivity implements VideosRecyclerView.ItemClickListener {
  private Button uploadVideo;
  private static Integer RESULT_LOAD_VIDEO = 9;
  private MainApplication mainApplication;
  private FileUtils fileUtils;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_videos);

    ArrayList<Bitmap> data = new ArrayList<>();
    mainApplication = ((MainApplication)getApplication());
    fileUtils = new FileUtils(getApplicationContext());

    // set up the RecyclerView
    RecyclerView recyclerView = findViewById(R.id.videos_recycler_view);
    int noOfColumns = 3;
    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), noOfColumns));
    VideosRecyclerView recyclerViewAdapter = new VideosRecyclerView(getApplicationContext(), data, mainApplication);
    recyclerViewAdapter.setClickListener(this);
    recyclerView.setAdapter(recyclerViewAdapter);

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

  @Override
  protected void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      final String videoUrl = data.getData().getPath();
      Video video = new Video();

      Uri videoUri = data.getData();

      String fileManagerPath = videoUri.getPath();

      String videoPath = uriToPath(videoUri);
      if (videoPath != null) {
        MainApplication application = ((MainApplication)getApplication());
        application.setVideoUrl(videoUrl);
        this.copyVideo(videoPath);

        Intent intent = new Intent(Videos.this, VideoEditor.class);
        intent.putExtra("videoPath", videoPath);
        startActivity(intent);
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
}
