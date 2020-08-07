package com.silviaodwyer.inversion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.takusemba.spotlight.OnTargetListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.Target;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.takusemba.spotlight.effet.RippleEffect;
import com.takusemba.spotlight.shape.Circle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.cyberagent.android.gpuimage.GPUImage;

import static android.graphics.Color.argb;

// TODO check if images have been moved or deleted from original locations

public class Images extends AppCompatActivity implements ImagesRecyclerView.ItemClickListener {
  private Button uploadImage;
  private Integer RESULT_LOAD_IMG = 8;
  private ImagesRecyclerView adapter;
  private ArrayList<ImageMetadata> savedFileMetaData;
  private MainApplication mainApplication;
  private Button deleteBtn;
  private RecyclerView recyclerView;
  private int numImages;
  private AlphaAnimation clickAnimation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_images);
    mainApplication = ((MainApplication) getApplication());
    savedFileMetaData = mainApplication.getSavedImageMetadata(this);

    // setup the Recycler View
    setupImages();
    clickAnimation = new AlphaAnimation(1F, 0.8F);
    uploadImage = findViewById(R.id.upload_img_btn);
    uploadImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        view.startAnimation(clickAnimation);
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
      }
    });

    deleteBtn = findViewById(R.id.delete_imgs);
    deleteBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        view.startAnimation(clickAnimation);
        FileUtils fileUtils = new FileUtils(getApplicationContext());
        File directory = new File(Environment.getExternalStorageDirectory() + "/Inversion/images");
        fileUtils.deleteDirectory(directory, getApplicationContext());
        Toast.makeText(getApplicationContext(), "Deleted: ", Toast.LENGTH_SHORT).show();
        getSavedImages();
        mainApplication.deleteAllMetadata(FileMetadata.FileType.IMAGE);
      }
    });
    this.getSavedImages();
  }

  private void setupImages() {
    // get num of images
    numImages = savedFileMetaData.size();

    ArrayList<ImageMetadata> imageMetadata = null;
    if (numImages == 0) {
      imageMetadata = mainApplication.getPlaceholderMetadata();

    }
    else {
      imageMetadata = savedFileMetaData;
    }
    initializeRecyclerView(imageMetadata);

  }

  private void initializeRecyclerView(ArrayList<ImageMetadata> imageMetadata) {
    recyclerView = findViewById(R.id.recycler_view);
    int numberOfColumns = 3;
    recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
    adapter = new ImagesRecyclerView(this, imageMetadata, mainApplication);
    adapter.setClickListener(this);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onResume() {
    super.onResume();
    ArrayList<ImageMetadata> savedImageMetadata = mainApplication.getSavedImageMetadata(getApplicationContext());
    adapter.updateRecyclerView(savedImageMetadata);
  }

  @Override
  protected void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      final Uri imageUri = data.getData();
      ImageUtils imageUtils = new ImageUtils(getApplicationContext());
      FileUtils fileUtils = new FileUtils(getApplicationContext());

      // set the image attribute for the application,
      MainApplication application = ((MainApplication)getApplication());
      String abs_path = fileUtils.getPathFromUri(imageUri);
      ImageMetadata metadata = new ImageMetadata(abs_path);
      Bitmap bitmap = imageUtils.imageUriToBitmap(imageUri);

      // Create new image and set it in MainApplication
      Image image = new Image(bitmap, getApplicationContext(), application.getImageEditorActivity(), metadata);
      application.setImage(image);
      Intent intent = new Intent(Images.this, ImageEditor.class);

      startActivity(intent);
    }

  }

  @Override
  public void onItemClick(View view, int position) {

  }

  public Bitmap getBitmapFromPath(String path) {
    Bitmap bitmap=null;

    try {
      File file = new File(path);

      BitmapFactory.Options bmOptions = new BitmapFactory.Options();

      bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

      bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, bmOptions);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return bitmap ;
  }

  private void getSavedImages() {
    if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
      File inversionDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/images");

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
