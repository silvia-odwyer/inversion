package com.silviaodwyer.inversion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
  private Bitmap bitmap;
  private Uri imageUri;
  private Bitmap originalImageBitmap;
  private ArrayList<Bitmap> filteredImages = new ArrayList<>();
  private MainApplication mainApplication;
  private GPUImageView gpuImageView;
  private Image image;

  @SuppressLint("WrongThread")
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

    gpuImageView = findViewById(R.id.gpuimageview);
    gpuImageView.setImage(image.getBitmap());
//    saveImageToImagePaths(image.getPath());
    originalImageBitmap = image.getOriginalImageBitmap();
    bitmap = image.getBitmap();
    saveImage();
  }

  public void updateImageView(Bitmap bmp) {
    imageView.setImageBitmap(bmp);
  }

  private void saveImageToImageNames(String image_name) {
      FileUtils fileUtils = new FileUtils(getApplicationContext());
      ArrayList<String> savedImagePaths = mainApplication.getSavedImageNames(getApplicationContext());
      String FILENAME = mainApplication.getSavedImagePathFilename();
      savedImagePaths.add(image_name);
      boolean isFilePresent = fileUtils.isFilePresent(FILENAME);

      String json = new Gson().toJson(savedImagePaths);
      Log.d("DEBUG", "Saved image names" + savedImagePaths.toString());

      // save
      fileUtils.writeFile(FILENAME, json);
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void updateGPUImage(GPUImageFilter filter) {
    gpuImageView.setFilter(filter);
  }

  public void saveImage() {

    ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

    File directory = contextWrapper.getDir(MainApplication.getImagesDirectory(), Context.MODE_PRIVATE);
    String image_name = generateImageName();

    this.saveImageToImageNames(image_name);
    File path = new File(directory, image_name);


    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(path);
      originalImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        fileOutputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String generateImageName() {
    Calendar calendar = Calendar.getInstance();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date = dateFormat.format(calendar.getTime());

    String image_name = "image_" + date + ".jpg";
    Log.d("DEBUG", "IMAGE NAME: " + image_name);
    return image_name;
  }

}
