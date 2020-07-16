package com.silviaodwyer.inversion.main_ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
  private ImagesRecyclerView adapter;
  private Activity activity;
  private ArrayList<ImageMetadata> savedImageMetadata;
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

    videosThumbnailsLinLayout = root.findViewById(R.id.videos);
    effectList = root.findViewById(R.id.effects);
    mainApplication = (MainApplication) activity.getApplication();
    savedImageMetadata = mainApplication.getSavedImageMetadata(context);

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
//      imageMetadata = mainApplication.getPlaceholderMetadata();
    }
    else {
      imageMetadata = savedImageMetadata;
    }
    initRecyclerViews(imageMetadata);

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


  @Override
  public void onResume() {
    super.onResume();
    Log.d("DEBUG", "HOME SCREEN FRAGMENT RESUMED");
    ArrayList<VideoMetadata> savedVideoMetadata = mainApplication.getSavedVideoMetadata(context);
    savedImageMetadata = mainApplication.getSavedImageMetadata(context);

    // if new images or videos detected, clear the thumbnails and re-append.
    if (savedVideoMetadata.size() != numVideos) {
      videosThumbnailsLinLayout.removeAllViews();
      this.appendVideoThumbnails();
    }
    if (savedImageMetadata.size() != numImages) {
      adapter.updateRecyclerView(savedImageMetadata);
//      this.appendImageThumbnails();
    }


  }

  private void initEffectList() {
    // adding sample effect names for now, effects will be imported via JSON file
    effectNames.add("Chromatic");
    effectNames.add("Sepia");
    effectNames.add("Vintage");

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
        Toast.makeText(getActivity().getApplicationContext(), "Opened images", Toast.LENGTH_SHORT).show();
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
    appendVideoThumbnails();

    // TODO setup RecyclerView for videos.
  }

  private void appendImageThumbnails() {
    ArrayList<ImageMetadata> savedFileMetadata = mainApplication.getSavedImageMetadata(context);

    int totalImageNum = 0;
    if (!savedFileMetadata.isEmpty()) {
      numImages = savedFileMetadata.size();

    }
    if (savedFileMetadata.size() < 4) {
      totalImageNum = savedFileMetadata.size();
    }
    else {
      totalImageNum = 4;
    }
    for (int i = 0; i < totalImageNum; i++) {
      final ImageMetadata metadata = savedFileMetadata.get(i);

      ImageView imageView = createImageView(metadata);

      imagesThumbnailsLinLayout.addView(imageView);
    }
  }

  private void appendVideoThumbnails() {
    ArrayList<VideoMetadata> savedFileMetadata = mainApplication.getSavedVideoMetadata(context);
    numVideos = savedFileMetadata.size();

    int totalImageNum = 0;
    if (savedFileMetadata.size() < 4) {
      totalImageNum = savedFileMetadata.size();
    }
    else {
      totalImageNum = 4;
    }

    for (int i = 0; i < totalImageNum; i++) {
      final VideoMetadata metadata = savedFileMetadata.get(i);

      ImageView imageView = createVideoThumbnail(metadata);
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          ImageUtils imageUtils = new ImageUtils(getActivity().getApplicationContext());

          Video video = new Video(metadata);
          mainApplication.setVideo(video);
          Intent intent = new Intent(getActivity().getBaseContext(), VideoEditor.class);
          File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos");

          String name = metadata.getName() + ".mp4";
          File file = new File(directory, name);

          String videoUrl = String.valueOf(Uri.fromFile(file));
          String videoPath = videoUrl;
          intent.putExtra("videoPath", videoPath);
          intent.putExtra("videoUrl", videoUrl);

          // Now that we have the bitmap thumbnail of the video, we can start the video editor activity.
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          getActivity().getApplication().startActivity(intent);
        }
      });
      videosThumbnailsLinLayout.addView(imageView);
    }
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
