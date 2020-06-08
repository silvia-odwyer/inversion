package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Images extends AppCompatActivity implements ImagesRecyclerView.ItemClickListener {
  private Button uploadImage;
  private Integer RESULT_LOAD_IMG = 8;
  private ImagesRecyclerView adapter;
  private ArrayList<String> savedImageNames;
  private MainApplication mainApplication;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_images);
    mainApplication = ((MainApplication)getApplication());
    savedImageNames = mainApplication.getSavedImageNames(this);

    initializeRecyclerView();

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

  private ArrayList<Bitmap> initializeSavedBitmaps() {
    ImageUtils imageUtils = new ImageUtils(this);
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    ContextWrapper contextWrapper = new ContextWrapper(this);
    File directory = contextWrapper.getDir(MainApplication.getImagesDirectory(), Context.MODE_PRIVATE);
    for (int position = 0; position < savedImageNames.size(); position++) {

      String imageName = savedImageNames.get(position);

      try {
        File file = new File(directory, imageName);
        final Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        if (bitmap == null) {
          Log.d("DEBUG", "BITMAP IS NULL");
        }
        else {

          int maxHeight = 250;
          int maxWidth = 250;
          final Bitmap resultBitmap = imageUtils.resizeBitmap(bitmap, maxWidth, maxHeight);
          bitmaps.add(resultBitmap);
        }

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    return bitmaps;

  }

  private void initializeRecyclerView() {
    ArrayList<Bitmap> bitmaps = initializeSavedBitmaps();

    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    int numberOfColumns = 3;
    recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
    adapter = new ImagesRecyclerView(this, bitmaps, mainApplication);
    adapter.setClickListener(this);
    recyclerView.setAdapter(adapter);
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

  @Override
  public void onItemClick(View view, int position) {

  }
}
