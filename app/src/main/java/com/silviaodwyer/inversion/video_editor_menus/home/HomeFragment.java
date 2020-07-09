package com.silviaodwyer.inversion.video_editor_menus.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daasuu.gpuv.egl.filter.GlFilter;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.utils.VideoFilters;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
  private ArrayList<GlFilter> filters;
  private VideoFilters videoFilters;
  private Bitmap originalVideoThumbnail;
  private View root;
  private MainApplication mainApplication;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    root = inflater.inflate(R.layout.fragment_video_editor_home, container, false);

    videoFilters = new VideoFilters(getActivity().getApplicationContext());
    filters = videoFilters.getVideoFilters();
    mainApplication = (MainApplication) getActivity().getApplication();

    originalVideoThumbnail = mainApplication.getVideo().getThumbnail();

    if (mainApplication.getVideo() == null) {
        Log.d("DEBUG", "VIDEO IS NULL");
    }

    if (originalVideoThumbnail == null) {
        Log.d("DEBUG", "THUMBNAIL IS NULL");
    }

    appendThumbnails();

//    ArrayList<Bitmap> thumbnails = videoFilters.generateThumbnails();
//    videoFilters.appendImageThumbnails(filteredImagesLinLayout, image, thumbnails);

    return root;
  }

  private void appendThumbnails() {

    LinearLayout filteredThumbnailsLinLayout = root.findViewById(R.id.filteredVideoThumbnails);
    for (int i = 0; i < filters.size(); i++) {
      final int index = i;
      ImageView imageView = new ImageView(getActivity().getApplicationContext());
      imageView.setImageBitmap(originalVideoThumbnail);
      filteredThumbnailsLinLayout.addView(imageView);

      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          videoFilters.filterVideo(filters.get(index), mainApplication.getPlayerView());
        }
      });
    }
  }
}
