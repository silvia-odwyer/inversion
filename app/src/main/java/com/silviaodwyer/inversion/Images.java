package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class Images extends AppCompatActivity {
  private Button uploadImage;
  private Integer RESULT_LOAD_IMG = 8;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_images);
    initGridView();

    uploadImage = findViewById(R.id.upload_img_btn);
    uploadImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
      }
    });
  }

  public void initGridView() {
    GridView gridView = (GridView) findViewById(R.id.images_grid_view);
    ImagesAdapter imagesAdapter = new ImagesAdapter(this);
    gridView.setAdapter(imagesAdapter);
  }

  @Override
  protected void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      final Uri imageUri = data.getData();
      // set the image attribute for the application,
      MainApplication application = ((MainApplication)getApplication());
      application.setImageUri(imageUri);
      Intent intent = new Intent(Images.this, ImageEditor.class);

      startActivity(intent);
    }
    else {
      // user has not chosen an image, display a Toast message
      Toast.makeText(this, "You haven't chosen an image.", Toast.LENGTH_LONG).show();
    }
  }
}
