package com.silviaodwyer.inversion.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.silviaodwyer.inversion.Images;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.Videos;

public class HomeFragment extends Fragment {
  private TextView viewImages;
  private TextView viewVideos;
  private View root;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    root = inflater.inflate(R.layout.fragment_home, container, false);

    viewImages = root.findViewById(R.id.view_images);
    viewVideos = root.findViewById(R.id.view_videos);
    setUpOnClickListeners();
    initImages();
    initVideos();

    return root;
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

  private void initImages() {
    LinearLayout imagesLinLayout = root.findViewById(R.id.images);

    appendPlaceholderImages(imagesLinLayout);
  }

  private void initVideos() {
    LinearLayout linLayout = root.findViewById(R.id.videos);

    appendPlaceholderImages(linLayout);
  }

  private void appendPlaceholderImages(LinearLayout linLayout) {
    // Create new ImageViews (with placeholders) and append them to the linear layout
    for (int i = 0; i < 6; i++) {
      ImageView newImage = new ImageView(getActivity().getApplicationContext());
      newImage.setImageResource(R.drawable.gradient);
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      layoutParams.setMargins(10, 10, 10, 10);
      newImage.setLayoutParams(layoutParams);
      newImage.setMinimumHeight(90);
      newImage.setMinimumWidth(90);
      linLayout.addView(newImage);
    }
  }
}
