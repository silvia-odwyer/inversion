package com.silviaodwyer.inversion.ui.image_editor_menus.home_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.ui.image_editor.ImageEditor;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;

public class HomeFragment extends Fragment {
  private Image image;
  private ImageEditor activity;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image_editor_home, container, false);
    activity = (ImageEditor) getActivity();
    MainApplication mainApplication = (MainApplication) getActivity().getApplication();

    image = new Image(mainApplication.getImage().getBitmap(), root.getContext(), activity, mainApplication.getImage().getMetaData());

//    GradientFilters gradientFilters = new GradientFilters(root.getContext());
//    LinearLayout filteredImagesLinLayout = root.findViewById(R.id.filteredImages);
//    ArrayList<ImageThumbnail> thumbnails = gradientFilters.getFilteredThumbnails(image.getOriginalImageBitmap());
//    gradientFilters.appendFilteredThumbnails(filteredImagesLinLayout, image, thumbnails);

    return root;
  }
}
