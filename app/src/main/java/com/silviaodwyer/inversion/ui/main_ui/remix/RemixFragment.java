package com.silviaodwyer.inversion.ui.main_ui.remix;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.silviaodwyer.inversion.models.ImageMetadata;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.adapters.WeeklyEditedImagesRecyclerView;

import java.util.ArrayList;

public class RemixFragment extends Fragment {
  private RecyclerView recyclerView;
  private View root;
  private WeeklyEditedImagesRecyclerView adapter;
  private MainApplication mainApplication;
  private Context context;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    root = inflater.inflate(R.layout.fragment_remix, container, false);
    Activity activity = getActivity();
    mainApplication = ((MainApplication) activity.getApplication());
    context = activity.getApplicationContext();

    this.initRecyclerView(mainApplication.getSavedImageMetadata(context));
    return root;
  }

  private void initRecyclerView(ArrayList<ImageMetadata> imageMetadata) {
    recyclerView = root.findViewById(R.id.imgs_recycler_view);
    int numberOfColumns = 3;
    recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
    adapter = new WeeklyEditedImagesRecyclerView(getActivity(), imageMetadata, mainApplication);

    recyclerView.setAdapter(adapter);
  }
}
