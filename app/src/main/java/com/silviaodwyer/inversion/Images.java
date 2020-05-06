package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

public class Images extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_images);

    initGridView();
  }

  public void initGridView() {
    GridView gridView = (GridView) findViewById(R.id.images_grid_view);
    ImagesAdapter imagesAdapter = new ImagesAdapter(this);
    gridView.setAdapter(imagesAdapter);
  }
}
