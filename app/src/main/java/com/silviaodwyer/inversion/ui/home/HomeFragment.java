package com.silviaodwyer.inversion.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_home, container, false);

    viewImages = root.findViewById(R.id.view_images);
    viewVideos = root.findViewById(R.id.view_videos);
    setUpOnClickListeners();

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
}
