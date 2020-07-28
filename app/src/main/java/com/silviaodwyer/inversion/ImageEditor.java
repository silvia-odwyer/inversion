package com.silviaodwyer.inversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ImageEditor extends AppCompatActivity {
  private Bitmap bitmap;
  private ArrayList<Bitmap> filteredImages = new ArrayList<>();
  private MainApplication mainApplication;
  private GPUImageView gpuImageView;
  private Image image;
  private ImageView imageView;
  private ImageFilters imageFilters;

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
    imageFilters = new ImageFilters(getApplicationContext());

    gpuImageView = findViewById(R.id.gpuimageview);

    bitmap = image.getOriginalImageBitmap();
    gpuImageView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
    gpuImageView.setRatio((float)bitmap.getWidth() / bitmap.getHeight());

    //    gpuImageView.setRatio((float) 0.99);
    gpuImageView.setImage(bitmap);

    ImageButton saveBtn = findViewById(R.id.save_btn);
    saveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // get permission to write to external storage
        mainApplication.requestPermissions(ImageEditor.this);
      }
    });

    initFilter();
    long startTime = mainApplication.getStartTime();
    long endTime = System.nanoTime();

    long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
    Log.d("DEBUG", "Time to open activity: " + duration);

    initTutorial();
  }

  public void initTutorial() {
    TapTargetView.showFor(this,
            TapTarget.forView( findViewById(R.id.save_btn), "Save your image", "This allows you to save your image.")
                    .targetRadius(75)
                    .outerCircleColor(R.color.colorPrimary)
                    .outerCircleAlpha(0.96f)
                    .targetCircleColor(R.color.backgroundColor)
                    .titleTextSize(20)
                    .titleTextColor(R.color.textColor)
                    .dimColor(R.color.backgroundColor)
                    .cancelable(false)
                    .drawShadow(true)
                    .tintTarget(false)
                    .transparentTarget(false),
            new TapTargetView.Listener() {
              @Override
              public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
              }
            });
  }

  public void initFilter() {
    Log.d("DEBUG", "IMAGE EDITOR METADATA: " + image.getMetaData().getAppliedFilter());
    String appliedFilter = image.getMetaData().getAppliedFilter();

    // if an applied filter exists, then filter; note an empty string signifies no filter
    if (!appliedFilter.equals("")) {
      GPUImageFilter filter = this.imageFilters.getFilterFromName(image.getMetaData().getAppliedFilter());
      updateGPUImage(filter);
    }
  }

  @Override
  public void onRequestPermissionsResult(int reqCode,
                                         String permissions[], int[] grantResults) {
    switch (reqCode) {
      case 1: {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          saveImage();
        } else {
          // permission was denied
        }
        return;
      }
    }
  }

  public ImageFilters getImageFilters() {
    return imageFilters;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void updateGPUImage(GPUImageFilter filter) {
     gpuImageView.setImage(bitmap);
    gpuImageView.setFilter(filter);
//    gpuImageView.forceSize = new GPUImageView.Size(300, 200);
  }

  public void saveImage() {
    FileOutputStream fileOutputStream = null;
    try {
      File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/images");
      directory.mkdirs();

      File outputFile = new File(directory.toString(), image.getMetaData().getName() + ".png");
      Log.d("DEBUG", "Saved image, path is: " + outputFile.getAbsolutePath());

      fileOutputStream = new FileOutputStream(outputFile);
      gpuImageView.getGPUImage().getBitmapWithFilterApplied().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

      mainApplication.saveImageMetadata(image.getMetaData());
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
  }

  public Image getImage() {
    return image;
  }

}