package com.silviaodwyer.inversion;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageAddBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageExclusionBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLaplacianFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSourceOverBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter;

public class ImageEditor extends AppCompatActivity {
  private ImageView imageView;
  private Bitmap bmp;
  private Uri imageUri;
  private Bitmap originalImageBitmap;
  private ArrayList<Bitmap> filteredImages = new ArrayList<>();
  private MainApplication mainApplication;
  private GPUImageView gpuImageView;
  private Image image;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_editor);
    BottomNavigationView navView = findViewById(R.id.nav_view);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupWithNavController(navView, navController);

    // set image
    mainApplication = ((MainApplication)getApplication());
    image = mainApplication.getImage();
    Log.d("DEBUG", "Absolute image path: " + image.getPath());

    imageUri = image.getImageUri();
    gpuImageView = findViewById(R.id.gpuimageview);
    gpuImageView.setImage(imageUri);
    saveImageToImagePaths(image.getPath());
  }

  public void updateImageView(Bitmap bmp) {
    imageView.setImageBitmap(bmp);
  }

  private void saveBitmaps(Uri imageUri) throws IOException {
    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
    originalImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
  }

  private void saveImageToImagePaths(String image_path) {
      FileUtils fileUtils = new FileUtils(getApplicationContext());
      ArrayList<String> savedImagePaths = mainApplication.getSavedImagePaths(getApplicationContext());
      String FILENAME = mainApplication.getSavedImagePathFilename();
      savedImagePaths.add(image_path);
      boolean isFilePresent = fileUtils.isFilePresent(FILENAME);

      String json = new Gson().toJson(savedImagePaths);
      Log.d("DEBUG", "Saved image paths" + savedImagePaths.toString());

      // save
      fileUtils.writeFile(FILENAME, json);
  }

  public Uri getImageURI() {
    return imageUri;
  }

  public void updateGPUImage(GPUImageFilter filter) {
    gpuImageView.setFilter(filter);
  }

}
