package com.silviaodwyer.inversion.main_ui.home;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.silviaodwyer.inversion.EffectDetail;
import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.ImageEditor;
import com.silviaodwyer.inversion.FileMetadata;
import com.silviaodwyer.inversion.ImageUtils;
import com.silviaodwyer.inversion.Images;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.SaveVideo;
import com.silviaodwyer.inversion.VideoEditor;
import com.silviaodwyer.inversion.VideoMetadata;
import com.silviaodwyer.inversion.Videos;

import java.io.File;
import java.util.ArrayList;

public class HomeScreenFragment extends Fragment {
  private TextView viewImages;
  private TextView viewVideos;
  private View root;
  private Context context;
  private Button openVideoSaver;
  private LinearLayout effectList;
  private MainApplication mainApplication;
  private ArrayList<String> effectNames = new ArrayList<String>();

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    root = inflater.inflate(R.layout.fragment_homescreen, container, false);
    context = getActivity().getApplicationContext();
    viewImages = root.findViewById(R.id.view_images);
    viewVideos = root.findViewById(R.id.view_videos);
    effectList = root.findViewById(R.id.effects);
    mainApplication = (MainApplication) getActivity().getApplication();

    setUpOnClickListeners();
    initImages();
    initVideos();
    initEffectList();

    return root;
  }

  private void initEffectList() {
    // adding sample effect names for now, effects will be imported via JSON file
    effectNames.add("Chromatic");
    effectNames.add("Sepia");
    effectNames.add("Vintage");

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

  private void initImages() {
    LinearLayout imagesLinLayout = root.findViewById(R.id.images);

    appendImageThumbnails(imagesLinLayout);
  }

  private void initVideos() {
    LinearLayout linLayout = root.findViewById(R.id.videos);

    appendVideoThumbnails(linLayout);
  }

  private void appendImageThumbnails(LinearLayout linLayout) {
    ArrayList<FileMetadata> savedFileMetadata = mainApplication.getSavedImageMetadata(context);

    int totalImageNum = 0;
    if (savedFileMetadata.size() < 4) {
      totalImageNum = savedFileMetadata.size();
    }
    else {
      totalImageNum = 4;
    }
    for (int i = 0; i < totalImageNum; i++) {
      final FileMetadata metadata = savedFileMetadata.get(i);

      ImageView imageView = createImageView(metadata);

      linLayout.addView(imageView);
    }
  }

  private void appendVideoThumbnails(LinearLayout linLayout) {
    ArrayList<VideoMetadata> savedFileMetadata = mainApplication.getSavedVideoMetadata(context);

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
      linLayout.addView(imageView);
    }
  }

  private ImageView createImageView(final FileMetadata metadata) {
    ImageView imageView = new ImageView(context);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(10, 10, 10, 10);
    imageView.setLayoutParams(layoutParams);

    File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/images");

    File file = new File(directory, metadata.getName());

    Glide
      .with(context)
      .load(file.getAbsolutePath())
      .apply(new RequestOptions().override(150, 150))
      .into(imageView);

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, ImageEditor.class);

        Image image = ImageUtils.getImageFromFilename(metadata, context, mainApplication);
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

    File file = new File(directory, metadata.getName());

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
