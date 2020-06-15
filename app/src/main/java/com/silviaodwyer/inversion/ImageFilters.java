package com.silviaodwyer.inversion;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHalftoneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVibranceFilter;

public class ImageFilters {
  private ArrayList<GPUImageFilter> filters;
  private ArrayList<GPUImageFilter> correctionFilters;

  public enum FilterType {
    EFFECT,
    CORRECTION,
  }

  public ImageFilters() {
    filters = new ArrayList<GPUImageFilter>();
    addEffectFilters();

    correctionFilters = new ArrayList<GPUImageFilter>();
    correctionFilters.add(new GPUImageBrightnessFilter(0.2f));
    correctionFilters.add(new GPUImageContrastFilter(0.9f));
    correctionFilters.add(new GPUImageGammaFilter());
  }

  public void addEffectFilters() {
    filters.add(new GPUImageSepiaToneFilter());
    filters.add(new GPUImageToonFilter());
    filters.add(new GPUImageSolarizeFilter());
    filters.add(new GPUImageGrayscaleFilter());
    filters.add(new GPUImageEmbossFilter());
    filters.add(new GPUImageVibranceFilter());
    filters.add(new GPUImageColorInvertFilter());
    filters.add(new GPUImageSketchFilter());
    filters.add(new GPUImagePosterizeFilter());
    filters.add(getDramaticFilter());
    filters.add(getObsidianFilter());
    filters.add(getVibrancyFilter());
    filters.add(getFourFilter());
    filters.add(getEightFilter());
    filters.add(getRubrikFilter());
  }

  public GPUImageFilterGroup getDramaticFilter() {
    float amount = (float) 1.4;
    float amount2 = (float) -0.3;

    GPUImageFilterGroup dramaticFilter = new GPUImageFilterGroup();
    dramaticFilter.addFilter(new GPUImageGrayscaleFilter());
    dramaticFilter.addFilter(new GPUImageContrastFilter(amount));
    dramaticFilter.addFilter(new GPUImageBrightnessFilter(amount2));
    return dramaticFilter;
  }

  public GPUImageFilterGroup getRubrikFilter() {
    float amount = (float) 1.1;
    float amount2 = (float) 1.2;

    GPUImageFilterGroup rubrikFilter = new GPUImageFilterGroup();
    rubrikFilter.addFilter(new GPUImageContrastFilter(amount));
    rubrikFilter.addFilter(new GPUImageBrightnessFilter(amount2));
    return rubrikFilter;
  }

  public GPUImageFilterGroup getEightFilter() {
    float amount = (float) 0.8;
    float amount2 = (float) -0.2;

    GPUImageFilterGroup eightFilter = new GPUImageFilterGroup();
    eightFilter.addFilter(new GPUImageSaturationFilter(amount));
    eightFilter.addFilter(new GPUImageBrightnessFilter(amount2));
    return eightFilter;
  }


  public GPUImageFilterGroup getFourFilter() {
    float amount = (float) 1.2;
    float amount2 = (float) -0.4;

    GPUImageFilterGroup fourFilter = new GPUImageFilterGroup();
    fourFilter.addFilter(new GPUImageSaturationFilter(amount));
    fourFilter.addFilter(new GPUImageBrightnessFilter(amount2));
    fourFilter.addFilter(new GPUImageContrastFilter(amount2));

    return fourFilter;
  }

  public GPUImageFilterGroup getObsidianFilter() {
    float amount = (float) 1.4;
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(new GPUImageGrayscaleFilter());
    filterGroup.addFilter(new GPUImageContrastFilter(amount));
    return filterGroup;
  }

  public GPUImageFilterGroup getVibrancyFilter() {
    float amount = (float) 1.1;
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(new GPUImageContrastFilter(amount));
    filterGroup.addFilter(new GPUImageVibranceFilter());
    return filterGroup;
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

  public ArrayList<Bitmap> generateThumbnails(final Image image, ImageFilters.FilterType filterType) {
    final ArrayList<Bitmap> thumbnails = new ArrayList<Bitmap>();

    switch(filterType) {
      case EFFECT:
        filters = getFilters();
        break;
      case CORRECTION:
        filters = getCorrectionFilters();
        break;
    }
    Bitmap thumbnail = image.getThumbnail(150, 150);

    GPUImage.getBitmapForMultipleFilters(thumbnail, filters, new GPUImage.ResponseListener<Bitmap>() {

      @Override
      public void response(Bitmap resultBitmap) {
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
