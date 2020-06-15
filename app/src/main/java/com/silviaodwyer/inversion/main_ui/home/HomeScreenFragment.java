package com.silviaodwyer.inversion.main_ui.home;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silviaodwyer.inversion.EffectDetail;
import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.ImageEditor;
import com.silviaodwyer.inversion.ImageMetadata;
import com.silviaodwyer.inversion.ImageUtils;
import com.silviaodwyer.inversion.Images;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.VideoEditor;
import com.silviaodwyer.inversion.Videos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class HomeScreenFragment extends Fragment {
  private TextView viewImages;
  private TextView viewVideos;
  private View root;
  private Context context;
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

    appendPlaceholderImages(imagesLinLayout, "Images");
  }

  private void initVideos() {
    LinearLayout linLayout = root.findViewById(R.id.videos);

//    appendPlaceholderImages(linLayout, "Videos");
  }

  private void appendPlaceholderImages(LinearLayout linLayout, final String activity_type) {
    ArrayList<ImageMetadata> savedImageMetadata = mainApplication.getMetaDataArrayList(context);
    ImageUtils imageUtils = new ImageUtils(context);
    int totalImageNum = 0;
    if (savedImageMetadata.size() < 4) {
      totalImageNum = savedImageMetadata.size();
    }
    else {
      totalImageNum = 4;
    }
    for (int i = 0; i < totalImageNum; i++) {
      ContextWrapper contextWrapper = new ContextWrapper(context);

      File directory = contextWrapper.getDir(mainApplication.getImagesDirectory(), Context.MODE_PRIVATE);
      String imageName = savedImageMetadata.get(i).getName();

      try {
        File file = new File(directory, imageName);
        final Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        if (bitmap == null) {
          Log.d("DEBUG", "BITMAP IS NULL");
        }
        else {
          int maxHeight = 150;
          int maxWidth = 150;
          Bitmap resultBitmap = imageUtils.resizeBitmap(bitmap, maxWidth, maxHeight);
          ImageView imageView = createImageView(resultBitmap, bitmap, savedImageMetadata.get(i));
          linLayout.addView(imageView);
        }

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  private ImageView createImageView(Bitmap resultBitmap, final Bitmap originalSizeBitmap, final ImageMetadata metadata) {
    ImageView imageView = new ImageView(context);

    imageView.setImageBitmap(resultBitmap);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(10, 10, 10, 10);
    imageView.setLayoutParams(layoutParams);
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, ImageEditor.class);

        Image image = new Image(originalSizeBitmap, context, mainApplication.getImageEditorActivity(), metadata);
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
}
