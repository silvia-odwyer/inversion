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
import com.silviaodwyer.inversion.ImageThumbnail;
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

    return root;
  }

}