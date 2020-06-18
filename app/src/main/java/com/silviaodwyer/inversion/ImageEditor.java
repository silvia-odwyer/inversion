package com.silviaodwyer.inversion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
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
  private Bitmap bitmap;
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
    bitmap = image.getBitmap();

    ImageView saveBtn = findViewById(R.id.save_btn);
    saveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveImage();
      }
    });
  }

  @Override
  public void onRequestPermissionsResult(int reqCode,
                                         String permissions[], int[] grantResults) {
    switch (reqCode) {
      case 1: {
        if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          FileOutputStream fileOutputStream = null;
          try {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/images");
            directory.mkdirs();

            File outputFile = new File(directory.toString(), image.getMetaData().getName());
            Log.d("DEBUG", "IMAGE OUTPUTTED TO: " + outputFile.getAbsolutePath());

            fileOutputStream = new FileOutputStream(outputFile);
            gpuImageView.getGPUImage().getBitmapWithFilterApplied().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));

          }
          catch (IOException e) {
            Log.d("DEBUG", "Could not write file!" + e);
          }
          finally {
            try {
              fileOutputStream.close();
              Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
              getContentResolver().notifyChange(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);

            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        } else {
          // permission was denied
        }
        return;
      }
    }
  }


  public Bitmap getBitmap() {
    return bitmap;
  }

  public void updateGPUImage(GPUImageFilter filter) {
    gpuImageView.setFilter(filter);
  }

  public void saveImage() {
    ImageMetadata metadata = new ImageMetadata();
    image.setMetaData(metadata);
    mainApplication.saveImageMetadata(image.getMetaData());
    mainApplication.requestPermissions(ImageEditor.this);
  }

}
