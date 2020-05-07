package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

public class Videos extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_videos);

    initGridView();
  }

  public void initGridView() {
    GridView gridView = (GridView) findViewById(R.id.videos_grid_view);
    VideosAdapter videosAdapter = new VideosAdapter(this);
    gridView.setAdapter(videosAdapter);
  }
}
