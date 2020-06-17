package com.silviaodwyer.inversion.video_editor_menus.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.daasuu.epf.filter.GlFilter;
import com.silviaodwyer.inversion.ImageFilters;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.VideoFilters;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_video_editor_home, container, false);

    VideoFilters videoFilters = new VideoFilters();
    ArrayList<GlFilter> filters = videoFilters.getVideoFilters();

    LinearLayout filteredThumbnailsLinLayout = root.findViewById(R.id.filteredVideoThumbnails);
//    ArrayList<Bitmap> thumbnails = videoFilters.generateThumbnails();
//    videoFilters.appendImageThumbnails(filteredImagesLinLayout, image, thumbnails);

    return root;
  }
}
