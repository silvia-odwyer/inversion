package com.silviaodwyer.inversion.image_editor_menus.home_menu;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;

import com.silviaodwyer.inversion.HomeActivity;
import com.silviaodwyer.inversion.ImageEditor;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

  ArrayList<Bitmap> filteredImages = new ArrayList<>();
  ImageEditor activity;
  Bitmap originalImageBitmap;
  ArrayList<GPUImageFilter> filters;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image_editor_home, container, false);
    MainApplication mainApplication = new MainApplication();
    activity = (ImageEditor) getActivity();

    return root;
  }


}
