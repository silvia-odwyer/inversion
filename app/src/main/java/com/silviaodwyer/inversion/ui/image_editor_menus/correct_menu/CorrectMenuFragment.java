package com.silviaodwyer.inversion.image_editor_menus.correct_menu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.image_editor.ImageEditor;
import com.silviaodwyer.inversion.image_filters.ImageFilters;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;

public class CorrectMenuFragment extends Fragment {

  private ImageEditor activity;
  private Image image;
  private ImageFilters imageFilters;
  private TextView brightnessTextVal;
  private String[] correctionEffects = {"brightness", "contrast", "saturation"};
  private String activeFilter = correctionEffects[0];
  private View root;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    root = inflater.inflate(R.layout.imagecorrect_menu_fragment, container, false);
    activity = (ImageEditor) getActivity();
    MainApplication mainApplication = (MainApplication) getActivity().getApplication();

    image = new Image(mainApplication.getImage().getBitmap(), root.getContext(), activity, mainApplication.getImage().getMetaData());

    imageFilters = new ImageFilters(getContext());

    // set a change listener on the SeekBar
    SeekBar slider = root.findViewById(R.id.slider);
    slider.setOnSeekBarChangeListener(sliderChangeListener);

    for (int index = 0; index < correctionEffects.length; index++) {
      int id = getResources().getIdentifier("btn_" + correctionEffects[index], "id", activity.getPackageName());
      ImageButton imgBtn = (ImageButton) root.findViewById(id);
      imgBtn.setOnClickListener(onClickListener);
    }

    return root;
  }

  ImageButton.OnClickListener onClickListener = new ImageButton.OnClickListener() {
    @Override
    public void onClick(View view) {
      for (int index = 0; index < correctionEffects.length; index++) {
        int id = getResources().getIdentifier("btn_" + correctionEffects[index], "id", activity.getPackageName());
        ImageButton imgBtn = (ImageButton) root.findViewById(id);
        imgBtn.setBackgroundColor(Color.TRANSPARENT);

      }
      view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
      activeFilter = view.getTag().toString();
    }
  };

  SeekBar.OnSeekBarChangeListener sliderChangeListener = new SeekBar.OnSeekBarChangeListener() {

    @Override
    public void onProgressChanged(SeekBar slider, int progress, boolean fromUser) {

      switch (activeFilter) {
        case "brightness":
          imageFilters.brightenImage(image, (float) (progress / 100.0));
          break;
        case "contrast":
          imageFilters.adjustContrast(image, (float) (progress / 100.0));
          break;
        case "saturation":
          imageFilters.adjustSaturation(image, (float) (progress / 100.0));
          break;
      }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
  };
}
