package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Images extends AppCompatActivity {
  private Button uploadImage;
  private Integer RESULT_LOAD_IMG = 8;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_images);

    MainApplication mainApplication = ((MainApplication)getApplication());

    uploadImage = findViewById(R.id.upload_img_btn);
    uploadImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
      }
    });
    initGridView();
  }

  public void initGridView() {
    MainApplication application = ((MainApplication)getApplication());
    GridView gridView = (GridView) findViewById(R.id.images_grid_view);
    ImagesAdapter imagesAdapter = new ImagesAdapter(this, application);
    gridView.setAdapter(imagesAdapter);
  }

  @Override
  protected void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      final Uri imageUri = data.getData();
      ImageUtils imageUtils = new ImageUtils(getApplicationContext());
      Bitmap bitmap = imageUtils.imageUriToBitmap(imageUri);
      Log.d("DEBUG", "Image URI: " + imageUri);

      // set the image attribute for the application,
      MainApplication application = ((MainApplication)getApplication());
      Image image = new Image(bitmap, getApplicationContext(), application.getImageEditorActivity());
      application.setImage(image);
      Intent intent = new Intent(Images.this, ImageEditor.class);

      startActivity(intent);
    }
    else {
      // user has not chosen an image, display a Toast message
      Toast.makeText(this, "No image chosen.", Toast.LENGTH_LONG).show();
    }
  }

}
