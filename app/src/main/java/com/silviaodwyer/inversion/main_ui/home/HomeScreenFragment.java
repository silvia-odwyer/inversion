package com.silviaodwyer.inversion.main_ui.home;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.silviaodwyer.inversion.EffectDetail;
import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.ImageEditor;
import com.silviaodwyer.inversion.FileMetadata;
import com.silviaodwyer.inversion.ImageMetadata;
import com.silviaodwyer.inversion.ImageUtils;
import com.silviaodwyer.inversion.Images;
import com.silviaodwyer.inversion.ImagesRecyclerView;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.Video;
import com.silviaodwyer.inversion.VideoEditor;
import com.silviaodwyer.inversion.VideoMetadata;
import com.silviaodwyer.inversion.Videos;
import com.silviaodwyer.inversion.VideosRecyclerView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeScreenFragment extends Fragment {
  private TextView viewImages;
  private TextView viewVideos;
  private View root;
  private Context context;
  private Button openVideoSaver;
  private LinearLayout imagesThumbnailsLinLayout;
  private LinearLayout videosThumbnailsLinLayout;
  private RecyclerView recyclerView;
  private RecyclerView videosRecyclerView;
  private VideosRecyclerView videosAdapter;
  private ImagesRecyclerView adapter;
  private Activity activity;
  private ArrayList<ImageMetadata> savedImageMetadata;
  private ArrayList<VideoMetadata> savedVideoMetadata;
  private LinearLayout effectList;
  private MainApplication mainApplication;
  private ArrayList<String> effectNames = new ArrayList<String>();
  private int numImages;
  private int numVideos;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    root = inflater.inflate(R.layout.fragment_homescreen, container, false);
    activity = getActivity();
    context = activity.getApplicationContext();
    viewImages = root.findViewById(R.id.view_images);
    viewVideos = root.findViewById(R.id.view_videos);

    effectList = root.findViewById(R.id.effects);
    mainApplication = (MainApplication) activity.getApplication();
    savedImageMetadata = mainApplication.getSavedImageMetadata(context);
    savedVideoMetadata = mainApplication.getSavedVideoMetadata(context);

    initTheme();
    setUpOnClickListeners();
    setUpImages();
    initVideos();
    initEffectList();

    mainApplication.requestPermissions(getActivity());

    return root;
  }

  private void setUpImages() {
    ImageUtils imageUtils = new ImageUtils(context);
    // get num of images
    numImages = savedImageMetadata.size();
    Log.d("DEBUG", "NUM IMAGES: " + numImages);
    ArrayList<ImageMetadata> imageMetadata = null;
    if (numImages == 0) {
      imageMetadata = mainApplication.getPlaceholderMetadata();
    }
    else {
      imageMetadata = savedImageMetadata;
    }
    initRecyclerViews(imageMetadata);

  }

  private void initVideoRecyclerView() {
      videosRecyclerView = root.findViewById(R.id.saved_videos_recycler_view);
      LinearLayoutManager layoutManager
            = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

      videosRecyclerView.setLayoutManager(layoutManager);
      videosAdapter = new VideosRecyclerView(activity, savedVideoMetadata, mainApplication);

      videosRecyclerView.setAdapter(videosAdapter);

      SnapHelper helper = new PagerSnapHelper();
      helper.attachToRecyclerView(videosRecyclerView);

  }

  private void initRecyclerViews(ArrayList<ImageMetadata> imageMetadata) {
    recyclerView = root.findViewById(R.id.recycler_view_images);
    LinearLayoutManager layoutManager
            = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

    recyclerView.setLayoutManager(layoutManager);
    adapter = new ImagesRecyclerView(activity, imageMetadata, mainApplication);

    recyclerView.setAdapter(adapter);

    SnapHelper helper = new PagerSnapHelper();
    helper.attachToRecyclerView(recyclerView);

  }

  private void initTheme() {
    SharedPreferences sharedPreferences = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
    if (! sharedPreferences.contains("nightThemeEnabled")) {
      UiModeManager umm = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
      umm.setNightMode(UiModeManager.MODE_NIGHT_YES);

      // save night mode to shared preferences
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putBoolean("nightThemeEnabled", true);
      editor.apply();
    }

  }


  @Override
  public void onResume() {
    super.onResume();
    Log.d("DEBUG", "HOME SCREEN FRAGMENT RESUMED");
    ArrayList<VideoMetadata> savedVideoMetadata = mainApplication.getSavedVideoMetadata(context);
    savedImageMetadata = mainApplication.getSavedImageMetadata(context);

    // if new images or videos detected, clear the thumbnails and re-append.
    if (savedVideoMetadata.size() != numVideos) {
      // update videos recyclerview
    }
    if (savedImageMetadata.size() != numImages) {
      adapter.updateRecyclerView(savedImageMetadata);
    }
  }

  private void initEffectList() {
    // adding sample effect names for now, effects will be imported via JSON file
    effectNames.add("Chromatic");
    effectNames.add("Sepia");
    effectNames.add("Vintage");
    effectNames.add("Vintage2");
    effectNames.add("Vintage3");
    effectNames.add("Vintage4");
    effectNames.add("Vintage5");
    effectNames.add("Vintage6");
    // import JSON file
    try {
      activity.getAssets().open("effects.json");
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < effectNames.size(); i++) {
      final String effectName = effectNames.get(i);

      TextView textView = new TextView(root.getContext());
      textView.setText(effectName);
      textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(getActivity(), EffectDetail.class);
          intent.putExtra("effectName", effectName);
          startActivity(intent);
        }
      });

      effectList.addView(textView);

    }
  }

  private void setUpOnClickListeners() {
    viewImages.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getActivity(), Images.class);
        startActivity(intent);
      }
    });

    viewVideos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getActivity(), Videos.class);
        startActivity(intent);
      }
    });

  }

  private void initVideos() {
    // TODO setup RecyclerView for videos.
    initVideoRecyclerView();

  }

  private ImageView createImageView(final ImageMetadata metadata) {
    ImageView imageView = new ImageView(context);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(10, 10, 10, 10);
    imageView.setLayoutParams(layoutParams);

    File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/images");

    File file = new File(directory, metadata.getName() + ".png");

    Glide
      .with(context)
      .load(file.getAbsolutePath())
      .apply(new RequestOptions().override(150, 150))
      .into(imageView);

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, ImageEditor.class);
        ImageUtils imageUtils = new ImageUtils(getActivity().getApplicationContext());
        Image image = imageUtils.getImageFromFilename(metadata, context, mainApplication);
        mainApplication.setImage(image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          // TODO implement shared animations and transitions

        } else {

        }
        startActivity(intent);

      }
    });

    return imageView;
  }

  private ImageView createVideoThumbnail(final FileMetadata metadata) {
    ImageView imageView = new ImageView(context);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(10, 10, 10, 10);
    imageView.setLayoutParams(layoutParams);

    File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos/thumbnails");

    File file = new File(directory, metadata.getName() + ".png");

    Glide
      .with(context)
      .load(file.getAbsolutePath())
      .apply(new RequestOptions().override(150, 150))
      .into(imageView);

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, VideoEditor.class);

//        Image image = ImageUtils.getImageFromFilename(metadata, context, mainApplication);
//        mainApplication.setImage(image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          // TODO implement shared animations and transitions

        } else {

        }
        startActivity(intent);
      }
    });

    return imageView;
  }
}
