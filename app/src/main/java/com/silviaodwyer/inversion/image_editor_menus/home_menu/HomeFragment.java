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
    final GPUImage mGPUImage = new GPUImage(root.getContext());

    Bitmap bmp = null;
    try {
      bmp = MediaStore.Images.Media.getBitmap(root.getContext().getContentResolver(), (activity.getImageURI()));
      originalImageBitmap = MediaStore.Images.Media.getBitmap(root.getContext().getContentResolver(), activity.getImageURI());
    } catch (IOException e) {
      e.printStackTrace();
    }
    mGPUImage.setImage(bmp);

    bmp = mGPUImage.getBitmapWithFilterApplied(bmp);
      filters = new ArrayList<GPUImageFilter>();

      filters.add(new GPUImageSepiaToneFilter());
      filters.add(new GPUImageToonFilter());
      mGPUImage.getBitmapForMultipleFilters(bmp,filters, new GPUImage.ResponseListener<Bitmap>() {

        @Override
        public void response(Bitmap resultBitmap) {
          int maxHeight = 150;
          int maxWidth = 150;
          float scale = Math.min(((float)maxHeight / resultBitmap.getWidth()), ((float)maxWidth / resultBitmap.getHeight()));
          // resize bitmap to thumbnail size
          Matrix matrix = new Matrix();
          matrix.postScale(scale, scale);

          resultBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);

          filteredImages.add(resultBitmap);
        }
      });

      for (int filter_index = 0; filter_index < filteredImages.size(); filter_index++) {
        final int filter_index_final = filter_index;
        final ImageView imageView = new ImageView(root.getContext());
        imageView.setImageBitmap(filteredImages.get(filter_index));
        imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
          filterImage(filters.get(filter_index_final), mGPUImage);
          }
        });
        LinearLayout filteredImagesLinLayout = root.findViewById(R.id.filteredImages);
        filteredImagesLinLayout.addView(imageView);
      }

    return root;
  }

  public void filterImage(GPUImageFilter filter, GPUImage gpuImage) {
    gpuImage.setFilter(filter);
    Bitmap bmp = gpuImage.getBitmapWithFilterApplied(originalImageBitmap);
    activity.updateGPUImage();
  }
}
