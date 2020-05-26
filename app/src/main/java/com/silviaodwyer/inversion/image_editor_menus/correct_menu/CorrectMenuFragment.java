package com.silviaodwyer.inversion.image_editor_menus.correct_menu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.ImageEditor;
import com.silviaodwyer.inversion.ImageFilters;
import com.silviaodwyer.inversion.R;

import java.util.ArrayList;

public class CorrectMenuFragment extends Fragment {

  private ImageEditor activity;
  private Image image;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.imagecorrect_menu_fragment, container, false);
    activity = (ImageEditor) getActivity();

    image = new Image(activity.getImageURI(), root.getContext(), activity);

    ImageFilters imageFilters = new ImageFilters();

    LinearLayout filteredImagesLinLayout = root.findViewById(R.id.correctedImageThumbnails);
    ArrayList<Bitmap> thumbnails = imageFilters.generateThumbnails(image, ImageFilters.FilterType.CORRECTION);
    imageFilters.appendImageThumbnails(filteredImagesLinLayout, image, thumbnails);

    return root;
  }
}
