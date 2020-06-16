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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
  private ArrayList<ImageMetadata> savedImageMetaData;
  private MainApplication mainApplication;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_images);
    mainApplication = ((MainApplication)getApplication());
    savedImageMetaData = mainApplication.getMetaDataArrayList(this);

    initializeRecyclerView();

    uploadImage = findViewById(R.id.upload_img_btn);
    uploadImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
      }
    });
  }

  private void initializeRecyclerView() {
    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    int numberOfColumns = 3;
    recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
    adapter = new ImagesRecyclerView(this, savedImageMetaData, mainApplication);
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
      ImageMetadata metadata = new ImageMetadata();
      Image image = new Image(bitmap, getApplicationContext(), application.getImageEditorActivity(), metadata);
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

  private ArrayList<ImageFile> initializeSavedBitmaps() {
    ImageUtils imageUtils = new ImageUtils(this);
    ArrayList<ImageFile> savedImages = new ArrayList<>();
    ContextWrapper contextWrapper = new ContextWrapper(this);
    File directory = contextWrapper.getDir(MainApplication.getImagesDirectory(), Context.MODE_PRIVATE);
    for (int position = 0; position < savedImageMetaData.size(); position++) {

      ImageMetadata metadata = savedImageMetaData.get(position);
      File file = new File(directory, metadata.getName());
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 4;
      final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

      if (bitmap == null) {
        Log.d("DEBUG", "BITMAP IS NULL");
      }
      else {
        ImageFile imageFile = new ImageFile(bitmap, metadata);
        savedImages.add(imageFile);

      }

    }
    return savedImages;
  }
}
