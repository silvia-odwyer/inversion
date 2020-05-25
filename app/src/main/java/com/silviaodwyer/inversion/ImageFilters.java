package com.silviaodwyer.inversion;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;

public class ImageFilters {
  private ArrayList<GPUImageFilter> filters;

  public ImageFilters() {
    filters = new ArrayList<GPUImageFilter>();
    filters.add(new GPUImageSepiaToneFilter());
    filters.add(new GPUImageToonFilter());
  }

  public ArrayList<GPUImageFilter> getFilters() {
    return filters;
  }

  public void setFilters(ArrayList<GPUImageFilter> filters) {
    this.filters = filters;
  }

  public void appendImageThumbnails(LinearLayout filteredImagesLinLayout, final Image image, ImageFilters imageFilters) {
    final ArrayList<GPUImageFilter> filters = imageFilters.getFilters();
    ArrayList<Bitmap> filteredImages = image.getFilteredThumbnails();

    final GPUImage mGPUImage = image.getmGPUImage();
    final Bitmap originalImageBitmap = image.getOriginalImageBitmap();
    final ImageEditor activity = image.getActivity();

    for (int filter_index = 0; filter_index < filteredImages.size(); filter_index++) {
      final int filter_index_final = filter_index;
      final ImageView imageView = new ImageView(image.getContext());
      imageView.setImageBitmap(filteredImages.get(filter_index));
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          filterImage(filters.get(filter_index_final), image);
        }
      });
      filteredImagesLinLayout.addView(imageView);
    }
  }


  public void filterImage(GPUImageFilter filter, Image image) {
    GPUImage gpuImage = image.getmGPUImage();
    ImageEditor activity = image.getActivity();
    gpuImage.setFilter(filter);
    Bitmap bmp = gpuImage.getBitmapWithFilterApplied(image.getOriginalImageBitmap());
    activity.updateGPUImage();
  }

}
