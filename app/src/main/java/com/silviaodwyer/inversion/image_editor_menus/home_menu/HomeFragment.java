package com.silviaodwyer.inversion.image_editor_menus.home_menu;

import android.app.Activity;
import android.content.Context;
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
import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.ImageEditor;
import com.silviaodwyer.inversion.ImageFilters;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
  private Bitmap originalImageBitmap;
  private Context context;
  private GPUImage mGPUImage;
  private Image image;
  private ImageEditor activity;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image_editor_home, container, false);
    MainApplication mainApplication = new MainApplication();
    activity = (ImageEditor) getActivity();
    mainApplication.setImageEditorActivity(activity);

    context = root.getContext();
    mGPUImage = new GPUImage(root.getContext());
    image = new Image(activity.getImageURI(), root.getContext());

    Bitmap bmp = null;
    try {
      bmp = MediaStore.Images.Media.getBitmap(root.getContext().getContentResolver(), (activity.getImageURI()));

      originalImageBitmap = image.getOriginalImageBitmap();
    } catch (IOException e) {
      e.printStackTrace();
    }
    mGPUImage.setImage(bmp);

    bmp = mGPUImage.getBitmapWithFilterApplied(bmp);

    ImageFilters imageFilters = new ImageFilters();
    ArrayList<GPUImageFilter> filters = imageFilters.getFilters();

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

         image.addFilteredThumbnail(resultBitmap);
      }
    });
    LinearLayout filteredImagesLinLayout = root.findViewById(R.id.filteredImages);

    appendImageThumbnails(filteredImagesLinLayout, image, imageFilters);

    return root;
  }

  public void filterImage(GPUImageFilter filter, GPUImage gpuImage, Bitmap originalImageBitmap, ImageEditor activity) {
    gpuImage.setFilter(filter);
    Bitmap bmp = gpuImage.getBitmapWithFilterApplied(originalImageBitmap);
    activity.updateGPUImage();
  }

  public void appendImageThumbnails(LinearLayout filteredImagesLinLayout, Image image, ImageFilters imageFilters) {
    final ArrayList<GPUImageFilter> filters = imageFilters.getFilters();
    ArrayList<Bitmap> filteredImages = image.getFilteredThumbnails();

    for (int filter_index = 0; filter_index < filteredImages.size(); filter_index++) {
      final int filter_index_final = filter_index;
      final ImageView imageView = new ImageView(context);
      imageView.setImageBitmap(filteredImages.get(filter_index));
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          filterImage(filters.get(filter_index_final), mGPUImage, originalImageBitmap, activity);
        }
      });
      filteredImagesLinLayout.addView(imageView);
    }
  }
}
