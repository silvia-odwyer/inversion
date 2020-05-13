package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class Videos extends AppCompatActivity {
  private Button uploadVideo;
  private static Integer RESULT_LOAD_VIDEO = 9;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_videos);
    initGridView();

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

  public void initGridView() {
    GridView gridView = (GridView) findViewById(R.id.videos_grid_view);
    VideosAdapter videosAdapter = new VideosAdapter(this);
    gridView.setAdapter(videosAdapter);
  }

  @Override
  protected void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      final String videoUrl = data.getData().getEncodedPath();
      // set the image attribute for the application,
      MainApplication application = ((MainApplication)getApplication());
      application.setVideoUrl(videoUrl);
      Log.d("DEBUG", "Video URL: " + videoUrl);

      // returning to Home Activity for now, since Video Activity not available.
      Intent intent = new Intent(Videos.this, VideoEditor.class);

      startActivity(intent);
    }
    else {
      // user has not chosen an image, display a Toast message
      Toast.makeText(this, "You haven't chosen a video.", Toast.LENGTH_LONG).show();
    }
  }
}
