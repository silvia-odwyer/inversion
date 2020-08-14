package com.silviaodwyer.inversion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static android.view.View.GONE;

public class ImageEditor extends AppCompatActivity {
  private Bitmap bitmap;
  private MainApplication mainApplication;
  private SharedPreferences sharedPreferences;
  private GPUImageView gpuImageView;
  private RecyclerView thumbnailsRecyclerView;
  private ImageThumbnailsRecyclerView adapter;
  private GradientFilters gradientFilters;
  private Image image;
  private HorizontalScrollView bottomNavBar;

  private ImageFilters imageFilters;
  private String[] navBtnNames;
  private String[] filterOverlayBtnNames;
  private ArrayList<ImageThumbnail> gradientGrayscaleThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> gradientThumbnails = new ArrayList<>();
  private Activity activity;

  @SuppressLint("WrongThread")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_editor);
    sharedPreferences = getApplicationContext().getSharedPreferences("PREF", Context.MODE_PRIVATE);
    activity = this;
    // set image
    mainApplication = ((MainApplication)getApplication());
    image = mainApplication.getImage();
    image.setActivity(this);
    imageFilters = new ImageFilters(getApplicationContext());



    navBtnNames = new String[]{"filters", "text", "effects", "correct"};
    filterOverlayBtnNames = new String[]{"gradients", "vintage", "glitch"};

    gpuImageView = findViewById(R.id.gpuimageview);

    bitmap = image.getOriginalImageBitmap();
    gpuImageView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
    gpuImageView.setRatio((float)bitmap.getWidth() / bitmap.getHeight());

    //    gpuImageView.setRatio((float) 0.99);
    gpuImageView.setImage(bitmap);
    mainApplication.setGpuImageView(gpuImageView);

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

    initThumbnailsRecyclerView();


      for (int index = 0; index < filterOverlayBtnNames.length; index++) {
          int id = getResources().getIdentifier("ie_overlay_btn_" + filterOverlayBtnNames[index], "id",
                  getPackageName());
          TextView ie_btn =  findViewById(id);
          ie_btn.setOnClickListener(filterBtnOlickListener);
      }
  }

  public void initNavBar() {
      bottomNavBar = findViewById(R.id.bottom_menu);
      for (int index = 0; index < navBtnNames.length; index++) {
          int id = getResources().getIdentifier("ie_btn_" + navBtnNames[index], "id",
                  getPackageName());
          LinearLayout ie_btn =  findViewById(id);
          ie_btn.setOnClickListener(onClickListener);
      }

  }

    TextView.OnClickListener filterBtnOlickListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int index = 0; index < filterOverlayBtnNames.length; index++) {
                int id = getResources().getIdentifier("ie_overlay_btn_" + filterOverlayBtnNames[index], "id", getPackageName());
                TextView btn = findViewById(id);
                btn.setTextColor(Color.GRAY);
            }

            TextView btn = findViewById(view.getId());
            btn.setTextColor(Color.WHITE);

            String nav_btn_tag = view.getTag().toString();

            switch (nav_btn_tag) {
                case "gradients": {
                    Log.d("DEBUG", "GRADIENTS BTN SELECTED");
                    if (gradientGrayscaleThumbnails.size() == 0) {
                        gradientGrayscaleThumbnails.addAll(gradientFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
                                gradientFilters.createGradientGrayscaleFilters()));
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            adapter.update(gradientGrayscaleThumbnails);

                        }
                    });

                    break;
                }
                case "vintage": {
                    Log.d("DEBUG", "VINTAGE BTN SELECTED");

                    if (gradientThumbnails.size() == 0) {
                        gradientThumbnails.addAll(gradientFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
                                gradientFilters.createGradientFilters()));
                    }
                    adapter.update(gradientThumbnails);
                    break;
                }
                case "glitch": {
                    Log.d("DEBUG", "GLITCH BTN SELECTED");

                    if (gradientGrayscaleThumbnails.size() == 0) {
                        gradientGrayscaleThumbnails.addAll(gradientFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
                                gradientFilters.createGradientGrayscaleFilters()));
                    }
                    adapter.update(gradientGrayscaleThumbnails);

                    break;
                }
            }
            thumbnailsRecyclerView.scheduleLayoutAnimation();
        }
    };

    ImageButton.OnClickListener onClickListener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int index = 0; index < navBtnNames.length; index++) {
                int id = getResources().getIdentifier("ie_btn_" + navBtnNames[index], "id", getPackageName());
                LinearLayout btn_layout = findViewById(id);
                btn_layout.setBackgroundColor(Color.TRANSPARENT);
            }
            view.setBackgroundColor(Color.BLACK);
            String nav_btn_tag = view.getTag().toString();

        switch (nav_btn_tag) {
          case "filters": {
              bottomNavBar.setVisibility(GONE);
//              LinearLayout overlay = findViewById(R.id.nav_overlay);
//              overlay.setVisibility(View.VISIBLE);

              if (gradientThumbnails.size() == 0) {
                  gradientThumbnails.addAll(gradientFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
                          gradientFilters.createGradientFilters()));
              }
              adapter.update(gradientThumbnails);
              break;
        }
          case "effects": {
            bottomNavBar.setVisibility(GONE);
//            LinearLayout overlay = findViewById(R.id.nav_overlay);
//            overlay.setVisibility(View.VISIBLE);
              if (gradientThumbnails.size() == 0) {
                  gradientThumbnails.addAll(gradientFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
                          gradientFilters.createGradientFilters()));
              }
              adapter.update(gradientThumbnails);
              break;
          }

      }
      thumbnailsRecyclerView.scheduleLayoutAnimation();
        }
    };

    public void filterImage(ImageThumbnail thumbnail) {
        gpuImageView.setImage(image.getOriginalImageBitmap());
        gpuImageView.setFilter((GPUImageFilter) thumbnail.getFilter());
    }

    public void initTutorial() {
    if (! sharedPreferences.contains("imageEditorTutorialCompleted")) {
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

      // save to shared preferences
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putBoolean("imageEditorTutorialCompleted", true);
      editor.apply();
    }

  }

  public void initFilter() {
    Log.d("DEBUG", "IMAGE EDITOR FILTER NAME: " + image.getMetaData().getAppliedFilter());
    String appliedFilter = image.getMetaData().getAppliedFilter();

    // if an applied filter exists, then filter; note an empty string signifies no filter
    if (!appliedFilter.equals("")) {
      GPUImageFilter filter = this.imageFilters.getFilterFromName(appliedFilter);

      if (filter != null) {
        updateGPUImage(filter);
      }
      else {
        Toast.makeText(getApplicationContext(), "Filter not found", Toast.LENGTH_SHORT).show();
      }
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

  private void initThumbnailsRecyclerView() {
    thumbnailsRecyclerView = findViewById(R.id.filtered_image_thumbnails);
    LinearLayoutManager layoutManager
            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
    thumbnailsRecyclerView.setLayoutManager(layoutManager);

    gradientFilters = new GradientFilters(getApplicationContext());

    List<List<Object>> filters = gradientFilters.createGradientFilters();

    gradientThumbnails = imageFilters.getFilteredThumbnails(image.getOriginalImageBitmap(), filters);
    adapter = new ImageThumbnailsRecyclerView(ImageEditor.this, gradientThumbnails, mainApplication, image);

    thumbnailsRecyclerView.setAdapter(adapter);

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