package com.silviaodwyer.inversion;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import jp.co.cyberagent.android.gpuimage.GPUImage;
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
  ImageView imageView;
  Bitmap bmp;
  Bitmap originalImageBitmap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_editor);
    BottomNavigationView navView = findViewById(R.id.nav_view);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupWithNavController(navView, navController);

    imageView = findViewById(R.id.imageView);

    // set image
    MainApplication application = ((MainApplication)getApplication());
    Uri imageUri = application.getImageUri();

    try {
      updateGPUImageView(imageUri);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void updateGPUImageView(Uri imageUri) throws IOException {
    saveBitmaps(imageUri);
    GPUImage gpuImage = new GPUImage(getApplicationContext());

    gpuImage.setImage(bmp);
    bmp = gpuImage.getBitmapWithFilterApplied(bmp);

    // Set the ImageView's image content to the current value of bmp
    imageView.setImageBitmap(bmp);

    final GPUImage mGPUImage = new GPUImage(this);
  }

  private void saveBitmaps(Uri imageUri) throws IOException {
    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
    originalImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
  }

}
