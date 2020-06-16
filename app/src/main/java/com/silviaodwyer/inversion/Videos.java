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
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class Videos extends AppCompatActivity implements VideosRecyclerView.ItemClickListener {
  private Button uploadVideo;
  private static Integer RESULT_LOAD_VIDEO = 9;
  private MainApplication mainApplication;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_videos);

    ArrayList<Bitmap> data = new ArrayList<>();
    mainApplication = ((MainApplication)getApplication());

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
  }

  @Override
  protected void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      final String videoUrl = data.getData().getPath();

      Uri videoUri = data.getData();

      String fileManagerPath = videoUri.getPath();

      String videoPath = uriToPath(videoUri);
      if (videoPath != null) {
        MainApplication application = ((MainApplication)getApplication());
        application.setVideoUrl(videoUrl);
        Log.d("DEBUG", "Video URL: " + videoUrl);

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
    String[] projection = { MediaStore.Video.Media.DATA };

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
}
