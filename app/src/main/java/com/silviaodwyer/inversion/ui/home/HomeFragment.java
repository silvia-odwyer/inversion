package com.silviaodwyer.inversion.ui.home;

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

import com.silviaodwyer.inversion.R;

public class HomeFragment extends Fragment {
  private TextView viewImages;
  private TextView viewVideos;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_home, container, false);
    final TextView textView = root.findViewById(R.id.text_home);

    viewImages = root.findViewById(R.id.view_images);
    viewVideos = root.findViewById(R.id.view_videos);
    setUpOnClickListeners();

    return root;
  }


  private void setUpOnClickListeners() {
    viewImages.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

      }
    });

    viewVideos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

      }
    });
  }


}
