package com.silviaodwyer.inversion;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHalftoneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVibranceFilter;

public class ImageFilters {
  private ArrayList<GPUImageFilter> filters;
  private ArrayList<GPUImageFilter> correctionFilters;

  public ImageFilters() {
    filters = new ArrayList<GPUImageFilter>();
    filters.add(new GPUImageSepiaToneFilter());
    filters.add(new GPUImageToonFilter());
    filters.add(new GPUImageSolarizeFilter());
    filters.add(new GPUImageGrayscaleFilter());
    filters.add(new GPUImageEmbossFilter());
    filters.add(new GPUImageVibranceFilter());

    correctionFilters = new ArrayList<GPUImageFilter>();
    correctionFilters.add(new GPUImageBrightnessFilter());
    correctionFilters.add(new GPUImageSaturationFilter());
  }

  public ArrayList<GPUImageFilter> getFilters() {
    return filters;
  }

  public void setFilters(ArrayList<GPUImageFilter> filters) {
    this.filters = filters;
  }

  public void addFilter(GPUImageFilter filter) {
    this.filters.add(filter);
  }

  public ArrayList<GPUImageFilter> getCorrectionFilters() {
    return correctionFilters;
  }

  public ArrayList<Bitmap> generateThumbnails(final Image image) {
    final ArrayList<Bitmap> thumbnails = new ArrayList<Bitmap>();

    GPUImage.getBitmapForMultipleFilters(image.getOriginalImageBitmap(), filters, new GPUImage.ResponseListener<Bitmap>() {

      @Override
      public void response(Bitmap resultBitmap) {
        int maxHeight = 150;
        int maxWidth = 150;
        float scale = Math.min(((float)maxHeight / resultBitmap.getWidth()), ((float)maxWidth / resultBitmap.getHeight()));
        // resize bitmap to thumbnail size
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        resultBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);

        thumbnails.add(resultBitmap);
      }
    });

    return thumbnails;
  }

  public void appendImageThumbnails(LinearLayout filteredImagesLinLayout, final Image image,
                                    ArrayList<Bitmap> filteredImages) {

    for (int index = 0; index < filteredImages.size(); index++) {
      final int index_final = index;
      final ImageView imageView = new ImageView(image.getContext());
      imageView.setImageBitmap(filteredImages.get(index));
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          filterImage(filters.get(index_final), image);
        }
      });
      filteredImagesLinLayout.addView(imageView);
    }
  }

  public void filterImage(GPUImageFilter filter, Image image) {
    GPUImage gpuImage = image.getmGPUImage();
    ImageEditor activity = image.getActivity();
    gpuImage.setFilter(filter);
    activity.updateGPUImage(filter);
  }

}
