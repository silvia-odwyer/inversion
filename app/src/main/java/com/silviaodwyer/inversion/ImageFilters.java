package com.silviaodwyer.inversion;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.*;

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
    filters.add(getCaliFilter());
    filters.add(getRetroFilter());
    filters.add(getRetroFilter2());
    filters.add(getRetroFilter3());
    filters.add(getRetroFilter4());
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

  public GPUImageFilterGroup getCaliFilter() {
    float amt = (float) 1.1;
    float amt2 = (float) 1.2;
    float r_amt = (float) 0.1;
    float g_amt = (float) 0.0;
    float b_amt = (float) 0.0;
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(new GPUImageSaturationFilter(amt));
    filterGroup.addFilter(new GPUImageRGBFilter(r_amt, g_amt, b_amt));
    filterGroup.addFilter(new GPUImageContrastFilter(amt2));

    return filterGroup;
  }

  public GPUImageFilterGroup getRetroFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();

//    GPUImageAddBlendFilter blendFilter = new GPUImageAddBlendFilter();
//    blendFilter.setBitmap(BitmapFactory.decodeResource(
//      getApplicationContext().getResources(), R.mipmap.background));
//    filterGroup.addFilter(blendFilter);

    GPUImageToneCurveFilter toneCurve = new GPUImageToneCurveFilter();
    PointF[] rControlPoints = new PointF[] { new PointF(0.0f, 0.145f),
            new PointF(0.063f, 0.153f), new PointF(0.251f, 0.278f),
            new PointF(0.573f, 0.776f), new PointF(0.624f, 0.863f),
            new PointF(0.682f, 0.922f), new PointF(0.792f, 0.965f),
            new PointF(1.0f, 1.0f) };

    PointF[] gControlPoints = new PointF[] { new PointF(0.0f, 0.0f),
            new PointF(0.255f, 0.196f), new PointF(0.447f, 0.576f),
            new PointF(0.686f, 0.875f), new PointF(1.0f, 1.0f) };

    PointF[] bControlPoints = new PointF[] { new PointF(0.0f, 0.137f),
            new PointF(0.251f, 0.251f), new PointF(0.345f, 0.376f),
            new PointF(0.608f, 0.698f), new PointF(0.890f, 0.91f),
            new PointF(1.0f, 0.941f) };

    toneCurve.setRedControlPoints(rControlPoints);
    toneCurve.setGreenControlPoints(gControlPoints);
    toneCurve.setBlueControlPoints(bControlPoints);

    filterGroup.addFilter(toneCurve);

    GPUImageSaturationFilter saturation = new GPUImageSaturationFilter();
    saturation.setSaturation(0.8f);
    filterGroup.addFilter(saturation);

    return filterGroup;
  }

  public GPUImageFilterGroup getRetroFilter2() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();

    GPUImageToneCurveFilter toneCurve = new GPUImageToneCurveFilter();
    PointF[] rControlPoints = new PointF[] { new PointF(0.0f, 0.145f),
            new PointF(0.063f, 0.153f), new PointF(0.251f, 0.278f),
            new PointF(0.573f, 0.776f), new PointF(0.624f, 0.863f),
            new PointF(0.682f, 0.922f), new PointF(0.792f, 0.965f),
            new PointF(1.0f, 1.0f) };

    PointF[] gControlPoints = new PointF[] { new PointF(0.0f, 0.0f),
            new PointF(0.255f, 0.196f), new PointF(0.447f, 0.576f),
            new PointF(0.686f, 0.875f), new PointF(1.0f, 1.0f) };

    PointF[] bControlPoints = new PointF[] { new PointF(0.0f, 0.137f),
            new PointF(0.251f, 0.251f), new PointF(0.345f, 0.376f),
            new PointF(0.608f, 0.698f), new PointF(0.890f, 0.91f),
            new PointF(1.0f, 0.941f) };

    toneCurve.setRedControlPoints(rControlPoints);
    toneCurve.setGreenControlPoints(gControlPoints);
    toneCurve.setBlueControlPoints(bControlPoints);

    filterGroup.addFilter(toneCurve);

    GPUImageSaturationFilter saturation = new GPUImageSaturationFilter();
    saturation.setSaturation(0.8f);
    filterGroup.addFilter(saturation);

    GPUImageContrastFilter contrast = new GPUImageContrastFilter();
    contrast.setContrast(0.8f);
    filterGroup.addFilter(contrast);

    return filterGroup;
  }


  public GPUImageFilterGroup getRetroFilter3() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    float amt = (float) 0.8;

    GPUImageGrayscaleFilter filter = new GPUImageGrayscaleFilter();
    filterGroup.addFilter(filter);

    GPUImageToneCurveFilter toneCurve = new GPUImageToneCurveFilter();
    PointF[] rControlPoints = new PointF[] { new PointF(0.0f, 0.145f),
            new PointF(0.063f, 0.153f), new PointF(0.251f, 0.278f),
            new PointF(0.573f, 0.776f), new PointF(0.624f, 0.863f),
            new PointF(0.682f, 0.922f), new PointF(0.792f, 0.965f),
            new PointF(1.0f, 1.0f) };

    PointF[] gControlPoints = new PointF[] { new PointF(0.0f, 0.0f),
            new PointF(0.255f, 0.196f), new PointF(0.447f, 0.576f),
            new PointF(0.686f, 0.875f), new PointF(1.0f, 1.0f) };

    PointF[] bControlPoints = new PointF[] { new PointF(0.0f, 0.137f),
            new PointF(0.251f, 0.251f), new PointF(0.345f, 0.376f),
            new PointF(0.608f, 0.698f), new PointF(0.890f, 0.91f),
            new PointF(1.0f, 0.941f) };

    toneCurve.setRedControlPoints(rControlPoints);
    toneCurve.setGreenControlPoints(gControlPoints);
    toneCurve.setBlueControlPoints(bControlPoints);

    filterGroup.addFilter(toneCurve);

    GPUImageSaturationFilter saturation = new GPUImageSaturationFilter();
    saturation.setSaturation(0.8f);
    filterGroup.addFilter(saturation);

    return filterGroup;
  }


  public GPUImageFilterGroup getRetroFilter4() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    float amt = (float) 0.8;

    GPUImageGrayscaleFilter filter = new GPUImageGrayscaleFilter();
    filterGroup.addFilter(filter);

    GPUImageToneCurveFilter toneCurve = new GPUImageToneCurveFilter();
    PointF[] rControlPoints = new PointF[] { new PointF(0.0f, 0.145f),
            new PointF(0.063f, 0.153f), new PointF(0.251f, 0.278f),
            new PointF(0.573f, 0.79f), new PointF(0.624f, 0.863f),
            new PointF(0.682f, 0.22f), new PointF(0.792f, 0.965f),
            new PointF(1.0f, 1.0f) };

    PointF[] gControlPoints = new PointF[] { new PointF(0.0f, 0.0f),
            new PointF(0.255f, 0.196f), new PointF(0.447f, 0.576f),
            new PointF(0.686f, 0.875f), new PointF(1.0f, 1.0f) };

    PointF[] bControlPoints = new PointF[] { new PointF(0.0f, 0.137f),
            new PointF(0.51f, 0.51f), new PointF(0.15f, 0.376f),
            new PointF(0.08f, 0.98f), new PointF(0.190f, 0.91f),
            new PointF(1.0f, 0.941f) };

    toneCurve.setRedControlPoints(rControlPoints);
    toneCurve.setGreenControlPoints(gControlPoints);
    toneCurve.setBlueControlPoints(bControlPoints);

    filterGroup.addFilter(toneCurve);

    GPUImageSaturationFilter saturation = new GPUImageSaturationFilter();
    saturation.setSaturation(0.8f);
    filterGroup.addFilter(saturation);

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
    ImageEditor activity = image.getActivity();
    activity.updateGPUImage(filter);
  }

  public void brightenImage(Image image, float amt) {
    GPUImageFilter brightnessFilter = new GPUImageBrightnessFilter(amt);
    filterImage(brightnessFilter, image);
  }

  public void adjustSaturation(Image image, float amt) {
    GPUImageFilter saturationFilter = new GPUImageSaturationFilter(amt);
    filterImage(saturationFilter, image);
  }

  public void adjustContrast(Image image, float amt) {
    Log.d("DEBUG", "ADJUST CONTRAST");
    GPUImageFilter contrastFilter = new GPUImageContrastFilter((float) 0.9);
    filterImage(contrastFilter, image);
  }
}
