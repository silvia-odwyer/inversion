package com.silviaodwyer.inversion.image_editor_menus.correct_menu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.ImageEditor;
import com.silviaodwyer.inversion.ImageFile;
import com.silviaodwyer.inversion.ImageFilters;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;

import java.util.ArrayList;

public class CorrectMenuFragment extends Fragment {

  private ImageEditor activity;
  private Image image;
  private ImageFilters imageFilters;
  private TextView brightnessTextVal;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.imagecorrect_menu_fragment, container, false);
    activity = (ImageEditor) getActivity();
    MainApplication mainApplication = (MainApplication) getActivity().getApplication();

    image = new Image(mainApplication.getImage().getBitmap(), root.getContext(), activity, mainApplication.getImage().getMetaData());

    imageFilters = new ImageFilters();

//    LinearLayout filteredImagesLinLayout = root.findViewById(R.id.correctedImageThumbnails);
//    ArrayList<Bitmap> thumbnails = imageFilters.generateThumbnails(image, ImageFilters.FilterType.CORRECTION);
//    imageFilters.appendImageThumbnails(filteredImagesLinLayout, image, thumbnails);

    // set a change listener on the SeekBar
    SeekBar brightnessSlider = root.findViewById(R.id.brightness);
    brightnessSlider.setOnSeekBarChangeListener(sliderChangeListener);

    SeekBar contrastSlider = root.findViewById(R.id.contrast);
    brightnessSlider.setOnSeekBarChangeListener(sliderChangeListener);

    SeekBar saturationSlider = root.findViewById(R.id.saturation);
    saturationSlider.setOnSeekBarChangeListener(sliderChangeListener);

    return root;
  }

  SeekBar.OnSeekBarChangeListener sliderChangeListener = new SeekBar.OnSeekBarChangeListener() {

    @Override
    public void onProgressChanged(SeekBar slider, int progress, boolean fromUser) {
      String sliderTag = slider.getTag().toString();

      switch (sliderTag) {
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
