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
import com.silviaodwyer.inversion.ImageFilters;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.utils.VideoFilters;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class HomeFragment extends Fragment {
  private List<List<Object>> filters;
  private VideoFilters videoFilters;
  private Bitmap originalVideoThumbnail;
  private View root;
  private MainApplication mainApplication;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    root = inflater.inflate(R.layout.fragment_video_editor_home, container, false);

    videoFilters = new VideoFilters(getActivity().getApplicationContext());
    filters = videoFilters.getEffectFilters();
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
    ImageFilters imageFilters = new ImageFilters(getContext());
    ArrayList<GPUImageFilter> correspondingImageFilters = videoFilters.getRequiredImageFilters();

    ArrayList<Bitmap> thumbnails = imageFilters.generateThumbnails(originalVideoThumbnail, correspondingImageFilters);
    for (int i = 0; i < thumbnails.size(); i++) {
      final int index = i;
      ImageView imageView = new ImageView(getActivity().getApplicationContext());
      imageView.setImageBitmap(thumbnails.get(i));
      filteredThumbnailsLinLayout.addView(imageView);

      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          videoFilters.filterVideo((GlFilter) filters.get(index).get(1), mainApplication.getPlayerView());
        }
      });
    }
  }
}
