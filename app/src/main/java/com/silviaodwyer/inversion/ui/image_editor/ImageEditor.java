package com.silviaodwyer.inversion.ui.image_editor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.silviaodwyer.inversion.image_filters.BlendEffectFilters;
import com.silviaodwyer.inversion.image_filters.GradientFilters;
import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.image_filters.ImageFilterPacks;
import com.silviaodwyer.inversion.image_filters.ImageFilterRetriever;
import com.silviaodwyer.inversion.image_filters.ImageFilters;
import com.silviaodwyer.inversion.ImageThumbnail;
import com.silviaodwyer.inversion.ImageThumbnailsRecyclerView;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

import static android.view.View.GONE;
import static android.view.View.generateViewId;

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
  private LinearLayout navEffectCategories;

  private ImageFilters imageFilters;
  private BlendEffectFilters blendEffectFilters;
  private String[] navBtnNames;
  private boolean isDarkTheme;
  private ArrayList<ImageThumbnail> gradientGrayscaleThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> glitchThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> gradientThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> popularThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> vintageThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> retroThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> dissolveThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> colorBlendThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> colorEffectsThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> otherEffectsThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> warmTintThumbnails = new ArrayList<>();
  private ArrayList<ImageThumbnail> coldTintThumbnails = new ArrayList<>();

  private Activity activity;
  private LinearLayout navOverlay;
  private Handler handler;
  private ImageFilterRetriever imageFilterRetriever;
  private ImageFilterPacks imageFilterPacks;

  @SuppressLint("WrongThread")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_editor);
    sharedPreferences = getApplicationContext().getSharedPreferences("PREF", Context.MODE_PRIVATE);
    isDarkTheme = sharedPreferences.getBoolean("nightThemeEnabled", isDarkTheme);
    activity = this;

    // set image
    mainApplication = ((MainApplication)getApplication());
    image = mainApplication.getImage();
    image.setActivity(this);
    imageFilters = new ImageFilters(getApplicationContext());
    gradientFilters = new GradientFilters(getApplicationContext());
    imageFilterRetriever = new ImageFilterRetriever(getApplicationContext());

    navBtnNames = new String[]{"filters", "glitch", "gradient_effects", "effects"};

    gpuImageView = findViewById(R.id.gpuimageview);

    bitmap = image.getOriginalImageBitmap();
    blendEffectFilters = new BlendEffectFilters(getApplicationContext(), bitmap);

    gpuImageView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
    gpuImageView.setRatio((float)bitmap.getWidth() / bitmap.getHeight());

    //    gpuImageView.setRatio((float) 0.99);
    gpuImageView.setImage(bitmap);
    mainApplication.setGpuImageView(gpuImageView);
    navOverlay = findViewById(R.id.nav_overlay);
    bottomNavBar = findViewById(R.id.bottom_menu);
    navEffectCategories = findViewById(R.id.navEffectCategories);
    handler = new Handler(new IncomingHandlerCallback());
    imageFilterPacks = new ImageFilterPacks(getApplicationContext(), bitmap);

    initClickListeners();
    initFilter();
    long startTime = mainApplication.getStartTime();
    long endTime = System.nanoTime();

    long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
    Log.d("DEBUG", "Time to open activity: " + duration);

    initTutorial();

    initThumbnailsRecyclerView();
    initNavBar();
    initIcons();
  }

  // change color of icons depending on light/dark theme enabled
  public void initIcons() {
      HashMap<String, Integer> iconNamesToRes = new HashMap<>();
      iconNamesToRes.put("filters", R.drawable.ic_compare_black_24dp);
      iconNamesToRes.put("glitch", R.drawable.ic_texture_black_36dp);
      iconNamesToRes.put("correct", R.drawable.ic_photo_filter_black_36dp);
      iconNamesToRes.put("effects", R.drawable.ic_photo_filter_black_36dp);
      iconNamesToRes.put("gradient_effects", R.drawable.ic_color_lens_black_36dp);

      // icons are light theme by default given app's theme is dark theme is default
      if (!isDarkTheme) {
          ImageButton saveBtn = findViewById(R.id.save_btn);
          saveBtn.setImageResource(R.drawable.save_btn_black_24dp);

          ImageButton backBtn = findViewById(R.id.back_btn);
          backBtn.setImageResource(R.drawable.arrow_back_black_24dp);

          for (String navBtnName : navBtnNames ) {
              int id = getResources().getIdentifier("ie_icon_" + navBtnName, "id", getPackageName());
              ImageView imageView = findViewById(id);
              imageView.setImageResource(iconNamesToRes.get(navBtnName));
          }
      }

  }

  public void initClickListeners() {
      ImageButton saveBtn = findViewById(R.id.save_btn);

      saveBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              // get permission to write to external storage
              mainApplication.requestPermissions(ImageEditor.this);
          }
      });

      ImageButton backBtn = findViewById(R.id.back_btn);
      backBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              // finish activity
              finish();
          }
      });
  }

  public void initNavBar() {

      for (int index = 0; index < navBtnNames.length; index++) {
          int id = getResources().getIdentifier("ie_btn_" + navBtnNames[index], "id",
                  getPackageName());
          LinearLayout ie_btn =  findViewById(id);
          ie_btn.setOnClickListener(onClickListener);
      }

  }

    TextView.OnClickListener filterBtnOnClickListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            bottomNavBar.setVisibility(GONE);
            for (int index = 0; index < navEffectCategories.getChildCount(); index++) {
                TextView btn = (TextView) navEffectCategories.getChildAt(index);
                btn.setTextColor(Color.GRAY);
            }

            TextView btn = findViewById(view.getId());
            btn.setTextColor(getResources().getColor(R.color.textColor));

            String nav_btn_tag = view.getTag().toString();
            Log.d("DEBUG", "TAG PRESSED: " + nav_btn_tag);

            switch (nav_btn_tag) {
                case "gradients": {
                    if (gradientThumbnails.size() == 0) {
                        gradientThumbnails.addAll(blendEffectFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
                                gradientFilters.getGradientFilters()));
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            adapter.update(gradientThumbnails);
                        }
                    });

                    break;
                }
                case "glitch": {
                    if (glitchThumbnails.size() == 0) {
                        glitchThumbnails.addAll(blendEffectFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
                                blendEffectFilters.createGlitchEffectFilters()));
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            adapter.update(glitchThumbnails);
                        }
                    });

                    break;
                }
                case "color fx": {
                    if (colorEffectsThumbnails.size() == 0) {
                        colorEffectsThumbnails.addAll(imageFilters.getFilteredThumbnails(bitmap,
                                imageFilterPacks.createColorEffectFilters()));
                    }
                    adapter.update(colorEffectsThumbnails);
                    break;
                }
                case "other fx": {
                    if (otherEffectsThumbnails.size() == 0) {
                        otherEffectsThumbnails.addAll(imageFilters.getFilteredThumbnails(bitmap,
                                imageFilterPacks.createOtherEffectsFilters()));
                    }
                    adapter.update(otherEffectsThumbnails);
                    break;
                }
                case "vintage": {
                    if (vintageThumbnails.size() == 0) {
                        vintageThumbnails.addAll(gradientFilters.getFilteredThumbnails(bitmap,
                                imageFilterPacks.createVintageFilters()));
                    }
                    adapter.update(vintageThumbnails);
                    break;
                }
                case "retro": {
                    if (retroThumbnails.size() == 0) {
                        retroThumbnails.addAll(gradientFilters.getFilteredThumbnails(bitmap,
                                imageFilterPacks.createRetroFilters()));
                    }
                    adapter.update(retroThumbnails);
                    break;
                }
                case "warm": {
                    if (warmTintThumbnails.size() == 0) {
                        warmTintThumbnails.addAll(gradientFilters.getFilteredThumbnails(bitmap,
                                imageFilterPacks.createWarmTintFilters()));
                    }
                    adapter.update(warmTintThumbnails);
                    break;
                }
                case "cold": {
                    if (coldTintThumbnails.size() == 0) {
                        coldTintThumbnails.addAll(gradientFilters.getFilteredThumbnails(bitmap,
                                imageFilterPacks.createColdTintFilters()));
                    }
                    adapter.update(coldTintThumbnails);
                    break;
                }
                case "popular": {
                    if (popularThumbnails.size() == 0) {
                        popularThumbnails.addAll(imageFilters.getFilteredThumbnails(bitmap,
                                imageFilterPacks.createPopularFilters()));
                    }
                    adapter.update(popularThumbnails);
                    break;
                }
                case "dissolve": {
                    if (dissolveThumbnails.size() == 0) {
                        dissolveThumbnails.addAll(gradientFilters.getFilteredThumbnails(bitmap,
                                gradientFilters.createGradientDissolveFilters()));
                    }
                    adapter.update(dissolveThumbnails);
                    break;
                }

                case "color blend": {
                    if (colorBlendThumbnails.size() == 0) {
                        colorBlendThumbnails.addAll(gradientFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
                                gradientFilters.getColorBlendFilters()));
                    }
                    adapter.update(colorBlendThumbnails);
                    break;
                }
                case "neon": {
                    adapter.clear();

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            if (gradientGrayscaleThumbnails.size() == 0) {
                                gradientGrayscaleThumbnails.addAll(gradientFilters.getFilteredThumbnails(image.getOriginalImageBitmap(),
                                        gradientFilters.createGradientGrayscaleFilters()));
                            }
                            Message msg = new Message();
                            msg.obj = gradientGrayscaleThumbnails;
                            handler.sendMessage(msg);
                        }
                    }).start();

                    break;
                }
            }
            thumbnailsRecyclerView.scheduleLayoutAnimation();
        }
    };

    ImageButton.OnClickListener onClickListener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            thumbnailsRecyclerView.setVisibility(View.VISIBLE);
            navOverlay.setVisibility(View.VISIBLE);

            for (String navBtnName : navBtnNames ) {
                int id = getResources().getIdentifier("ie_btn_" + navBtnName, "id", getPackageName());
                LinearLayout btn_layout = findViewById(id);
                btn_layout.setBackgroundColor(Color.TRANSPARENT);
            }

            String nav_btn_tag = view.getTag().toString();
            bottomNavBar.setVisibility(GONE);
            navEffectCategories.removeAllViews();

        switch (nav_btn_tag) {
          case "filters": {
              String[] gradientEffectNavNames = {"Popular", "Vintage", "Retro", "Warm", "Cold"};
              createCategoryMenu(gradientEffectNavNames);

              break;
        }
        case "glitch": {
                String[] gradientEffectNavNames = {"Glitch", "Color FX", "Other FX"};
                createCategoryMenu(gradientEffectNavNames);
                break;
            }
          case "gradient_effects": {
              String[] gradientEffectNavNames = {"Gradients", "Neon", "Dissolve", "Color Blend"};
              createCategoryMenu(gradientEffectNavNames);

              break;
          }

      }
      thumbnailsRecyclerView.scheduleLayoutAnimation();
        }
    };

    public void createCategoryMenu(String[] categoryNames) {

        for (String navName: categoryNames) {
            TextView textView = new TextView(getApplicationContext());
            navName = navName.toUpperCase();
            textView.setText(navName);
            textView.setId(generateViewId());
            textView.setOnClickListener(filterBtnOnClickListener);
            textView.setTag(navName.toLowerCase());
            textView.setTextColor(Color.GRAY);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(14, 12, 14, 14);
            textView.setLayoutParams(lp);
            navEffectCategories.addView(textView);
        }

        // set first category text view to white
        TextView firstCategoryTextView = (TextView) navEffectCategories.getChildAt(0);
        firstCategoryTextView.setTextColor(getResources().getColor(R.color.textColor));
    }

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
      GPUImageFilter filter = imageFilterRetriever.retrieveFilter(appliedFilter);

      if (filter != null) {
          Log.d("DEBUG", "FOUND FILTER, APPLYING ");
        updateGPUImage(filter);
      }
      else {
        Toast.makeText(getApplicationContext(), "Filter: " + appliedFilter + " not found", Toast.LENGTH_SHORT).show();
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

    List<List<Object>> filters = imageFilterPacks.createVintageFilters();

    vintageThumbnails = imageFilters.getFilteredThumbnails(image.getOriginalImageBitmap(), filters);
    adapter = new ImageThumbnailsRecyclerView(ImageEditor.this, vintageThumbnails, mainApplication, gpuImageView);

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

    class IncomingHandlerCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(Message message) {
            Log.d("DEBUG", "RECEIVED INCOMING THUMBNAILS");
            adapter.update((ArrayList<ImageThumbnail>) message.obj);
            thumbnailsRecyclerView.scheduleLayoutAnimation();

            return true;
        }
    }

  public Image getImage() {
    return image;
  }

  @Override
  public void onBackPressed() {
        if (bottomNavBar.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            navOverlay.setVisibility(GONE);
            thumbnailsRecyclerView.setVisibility(GONE);
            bottomNavBar.setVisibility(View.VISIBLE);
        }
  }

}