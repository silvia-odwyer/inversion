package com.silviaodwyer.inversion.main_ui.effects;

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

import com.silviaodwyer.inversion.FileMetadata;
import com.silviaodwyer.inversion.ImagesRecyclerView;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;

import java.util.ArrayList;

public class EffectsFragment extends Fragment {
  private RecyclerView recyclerView;
  private View root;
  private ImagesRecyclerView adapter;
  private MainApplication mainApplication;
  private Context context;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    root = inflater.inflate(R.layout.fragment_effects, container, false);
    Activity activity = getActivity();
    mainApplication = ((MainApplication) activity.getApplication());
    context = activity.getApplicationContext();

    this.initRecyclerView(mainApplication.getSavedImageMetadata(context));
    return root;
  }

  private void initRecyclerView(ArrayList<FileMetadata> imageMetadata) {
    recyclerView = root.findViewById(R.id.imgs_recycler_view);
    int numberOfColumns = 4;
    recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
    adapter = new ImagesRecyclerView(getActivity(), imageMetadata, mainApplication);

    recyclerView.setAdapter(adapter);
  }
}
