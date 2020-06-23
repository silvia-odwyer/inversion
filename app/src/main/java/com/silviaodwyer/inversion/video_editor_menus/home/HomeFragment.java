package com.silviaodwyer.inversion.video_editor_menus.home;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.daasuu.epf.filter.GlFilter;
import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.ImageFilters;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.VideoEditor;
import com.silviaodwyer.inversion.VideoFilters;
import com.silviaodwyer.inversion.VideoPlayer;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
  private ArrayList<GlFilter> filters;
  private VideoFilters videoFilters;
  private Bitmap originalVideoThumbnail;
  private View root;
  private MainApplication mainApplication;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    root = inflater.inflate(R.layout.fragment_video_editor_home, container, false);

    videoFilters = new VideoFilters();
    filters = videoFilters.getVideoFilters();
    mainApplication = (MainApplication) getActivity().getApplication();

    originalVideoThumbnail = mainApplication.getVideo().getThumbnail();

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
