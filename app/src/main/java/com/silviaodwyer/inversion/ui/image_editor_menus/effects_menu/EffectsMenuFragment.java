package com.silviaodwyer.inversion.ui.image_editor_menus.effects_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silviaodwyer.inversion.ui.image_editor.ImageEditor;
import com.silviaodwyer.inversion.image_filters.ImageFilters;
import com.silviaodwyer.inversion.R;

public class EffectsMenuFragment extends Fragment {
  private ImageFilters imageFilters;
  private ImageEditor activity;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.effects_menu_fragment, container, false);
    activity = (ImageEditor) getActivity();

    imageFilters = activity.getImageFilters();

    return root;
  }
}
