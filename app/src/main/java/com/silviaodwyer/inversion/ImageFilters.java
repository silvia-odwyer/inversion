package com.silviaodwyer.inversion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.signature.ObjectKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.*;

public class ImageFilters {
  private ArrayList<GPUImageFilter> filters;
  private ArrayList<GPUImageFilter> correctionFilters;
  private GPUImageFilterGroup activeFilterGroup = new GPUImageFilterGroup();
  private List<String> filterNames;
  private Context context;
  private List<Integer> gradient_backgrounds;
  private GPUImageBrightnessFilter brightnessFilter;
  private GPUImageContrastFilter contrastFilter;
  private GPUImageSaturationFilter saturationFilter;
  private List<List<Object>> effectFilters;

  public enum FilterType {
    EFFECT,
    CORRECTION,
  }

  public ImageFilters(Context context) {
    this.context = context;
    filters = new ArrayList<>();
    gradient_backgrounds = Arrays.asList(R.mipmap.summer, R.mipmap.atlantic, R.mipmap.cosmic,
            R.mipmap.lavender, R.mipmap.pink, R.mipmap.purple, R.mipmap.rainbow, R.mipmap.stars);

    filterNames = Arrays.asList("neonPink", "rubrik", "eight");


    addEffectFilters();
    initCorrectionFilters();
  }

  public ArrayList getFilters() {
      return this.filters;
  }

  public void initCorrectionFilters() {
    correctionFilters = new ArrayList<GPUImageFilter>();

    brightnessFilter = new GPUImageBrightnessFilter();
    contrastFilter = new GPUImageContrastFilter();
    saturationFilter = new GPUImageSaturationFilter();

    activeFilterGroup.addFilter(brightnessFilter);
    activeFilterGroup.addFilter(contrastFilter);
    activeFilterGroup.addFilter(saturationFilter);
  }

  private static GPUImageFilter createTwoBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> twoInputFilterClass,
                                                     int resource) {
    try {
      // create a two input filter
      GPUImageTwoInputFilter filter = twoInputFilterClass.newInstance();

      // set bitmap to blend with
      filter.setBitmap(BitmapFactory.decodeResource(context.getResources(), resource));
      return filter;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void addEffectFilters() {

    // create blend filters
      effectFilters = new ArrayList<>();

      effectFilters.add(Arrays.asList("Neon Pink", getNeonPinkFilter()));
      effectFilters.add(Arrays.asList("Orbiton", getOribitonFilter()));
      effectFilters.add(Arrays.asList("Obsidian", getObsidianFilter()));
      effectFilters.add(Arrays.asList("Grayscale", new GPUImageGrayscaleFilter()));
      effectFilters.add(Arrays.asList("Monochrome", new GPUImageMonochromeFilter()));
      effectFilters.add(Arrays.asList("Sharpen", new GPUImageSharpenFilter()));
      effectFilters.add(Arrays.asList("Aesthetica", getAestheticaFilter()));
      effectFilters.add(Arrays.asList("Dramatic", getDramaticFilter()));
      effectFilters.add(Arrays.asList("Sepia", new GPUImageSepiaToneFilter()));
      effectFilters.add(Arrays.asList("Solarize", new GPUImageSolarizeFilter()));

      for (List filter: effectFilters) {
          Log.d("DEBUG", "FILTER: " + filter.get(0));
          filters.add((GPUImageFilter) filter.get(1));
      }


  }

  private void createBlendFilters() {
    List<Integer> blend_backgrounds = Arrays.asList(R.mipmap.lensflare, R.mipmap.scrapbook,
            R.mipmap.galaxy, R.mipmap.background2);

    for (int k = 0; k < blend_backgrounds.size(); k++) {
      int background = blend_backgrounds.get(k);
      GPUImageFilter filter = createTwoBlendFilter(context, GPUImageAddBlendFilter.class, background);
      effectFilters.add(Arrays.asList("Blend " + k, filter));
      filters.add(filter);
    }
  }

  public void createGradientFilters() {
    for (int k = 0; k < gradient_backgrounds.size(); k++) {
      int background = gradient_backgrounds.get(k);
      GPUImageFilter filter = createTwoBlendFilter(context, GPUImageSoftLightBlendFilter.class, background);
      effectFilters.add(Arrays.asList("Gradient " + k, filter));
      filters.add(filter);
    }
  }

  public void createGradientGrayscaleFilters() {
    for (int k = 0; k < gradient_backgrounds.size(); k++) {
      GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
      filterGroup.addFilter(getObsidianFilter());
      int background = gradient_backgrounds.get(k);
      GPUImageFilter filter = createTwoBlendFilter(context, GPUImageSoftLightBlendFilter.class, background);
      filterGroup.addFilter(filter);
      effectFilters.add(Arrays.asList("Gradient BW " + k, filter));
      filters.add(filterGroup);
      
    }
  }

  private GPUImageFilterGroup getOribitonFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(getObsidianFilter());
    GPUImageFilter filter = createTwoBlendFilter(context, GPUImageSoftLightBlendFilter.class, R.mipmap.purple);
    filterGroup.addFilter(filter);
    return filterGroup;
  }

  private GPUImageFilterGroup getNeonPinkFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(getObsidianFilter());
    GPUImageFilter filter = createTwoBlendFilter(context, GPUImageSoftLightBlendFilter.class, R.mipmap.pink);
    filterGroup.addFilter(filter);
    return filterGroup;
  }

  private GPUImageFilterGroup getDramaticFilter() {
    float amount = (float) 1.4;
    float amount2 = (float) -0.3;

    GPUImageFilterGroup dramaticFilter = new GPUImageFilterGroup();
    dramaticFilter.addFilter(new GPUImageGrayscaleFilter());
    dramaticFilter.addFilter(new GPUImageContrastFilter(amount));
    dramaticFilter.addFilter(new GPUImageBrightnessFilter(amount2));
    return dramaticFilter;
  }

  private GPUImageFilterGroup getRubrikFilter() {
    float amount = (float) 0.8;
    float amount2 = (float) 0.2;

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
    float amt = (float) 0.7;
    float amt2 = (float) 1.2;
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(new GPUImageSaturationFilter(amt));
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


  private GPUImageFilterGroup getRetroFilter3() {
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

  private GPUImageFilterGroup getAestheticaFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    float amt = (float) 0.4;
    float exp_amt = (float) 0.05;
    filterGroup.addFilter(new GPUImageContrastFilter(amt));
    filterGroup.addFilter(new GPUImagePixelationFilter());
    return filterGroup;
  }

  private GPUImageFilterGroup getSaturnFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    float amt = (float) 0.4;
    float brightness_amt = (float) 0.15;
    filterGroup.addFilter(new GPUImageContrastFilter(amt));
    filterGroup.addFilter(new GPUImageBrightnessFilter(brightness_amt));
    filterGroup.addFilter(new GPUImageVibranceFilter(amt));
    return filterGroup;
  }

  private GPUImageFilterGroup getRetroVignette() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    float amt = (float) 0.4;
    float exp_amt = (float) 0.05;
    filterGroup.addFilter(getRetroFilter());
    filterGroup.addFilter(new GPUImageVignetteFilter());
    filterGroup.addFilter(new GPUImageVibranceFilter(exp_amt));
    return filterGroup;
  }

  public GPUImageFilterGroup getRetroVignette2() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    float exp_amt = (float) 0.15;
    filterGroup.addFilter(getRetroFilter2());
    filterGroup.addFilter(new GPUImageVignetteFilter());
    return filterGroup;
  }

  private GPUImageFilterGroup getOrbikFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    float amt = (float) 0.4;
    float exp_amt = (float) 0.05;
    filterGroup.addFilter(new GPUImageContrastFilter(amt));
    filterGroup.addFilter(new GPUImageSaturationFilter(amt));
    filterGroup.addFilter(new GPUImageVibranceFilter(exp_amt));
    return filterGroup;
  }

  public ArrayList<GPUImageFilter> getCorrectionFilters() {
    return correctionFilters;
  }

  public ArrayList<Bitmap> generateThumbnails(final Image image, ImageFilters.FilterType filterType) {
    final ArrayList<Bitmap> thumbnails = new ArrayList<Bitmap>();

//    switch(filterType) {
//      case EFFECT:
//        filters = getImageFilters();
//        break;
//      case CORRECTION:
//        filters = getCorrectionFilters();
//        break;
//    }
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
      final ImageView imageView = new ImageView(image.getContext());
      imageView.setImageBitmap(filteredImages.get(index));
      String filterName = (String) effectFilters.get(index).get(0);
      GPUImageFilter filter = (GPUImageFilter) effectFilters.get(index).get(1);

      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          filterImage(filter, image);
          image.getMetaData().setAppliedFilter(filterName);
        }
      });

      filteredImagesLinLayout.addView(imageView);
    }
  }

  public void filterImageFromName(String filterName, Image image) {
    GPUImageFilter filter = getFilterFromName(filterName);
    image.getMetaData().setAppliedFilter(filterName);

    ImageEditor activity = image.getActivity();
    activity.updateGPUImage(filter);
  }

  public GPUImageFilter getFilterFromName(String filterName) {
    GPUImageFilter filter = null;
    switch(filterName) {
      case "Neon Pink":
        filter = getNeonPinkFilter();
        break;
      case "Eight":
        filter = getEightFilter();
        break;
      case "Orbiton":
        filter = getOribitonFilter();
        break;
      case "Aesthetica":
        filter = getAestheticaFilter();
        break;
      case "Sepia":
        filter = new GPUImageSepiaToneFilter();
        break;
      case "Grayscale":
        filter = new GPUImageGrayscaleFilter();
        break;
      case "Vibrance":
        filter = new GPUImageVibranceFilter();
        break;
      case "Dramatic":
        filter = getDramaticFilter();
        break;
      case "Obsidian":
        filter = getObsidianFilter();
        break;
      case "Vibrancy":
        filter = getVibrancyFilter();
        break;
      case "Rubrik":
        filter = getRubrikFilter();
        break;
      case "Cali":
        filter = getCaliFilter();
        break;
      case "Retro":
        filter = getRetroFilter();
        break;
      case "Retro2":
        filter = getRetroFilter2();
        break;
      case "Retro3":
        filter = getRetroFilter3();
        break;
      case "Retro Vignette":
        filter = getRetroVignette();
        break;
      case "Retro Vignette2":
        filter = getRetroVignette2();
        break;
    }
    return filter;
  }

  public void filterImage(GPUImageFilter filter, Image image) {
    ImageEditor activity = image.getActivity();
    activity.updateGPUImage(filter);
  }

  public void addEffect(GPUImageFilter filter, Image image) {
    ImageEditor activity = image.getActivity();
    activity.updateGPUImage(activeFilterGroup);
  }

  public void brightenImage(Image image, float amt) {
    brightnessFilter.setBrightness(amt);
    addEffect(brightnessFilter, image);
  }

  public void adjustSaturation(Image image, float amt) {
    saturationFilter.setSaturation(amt);
    addEffect(saturationFilter, image);
  }

  public void adjustContrast(Image image, float amt) {
    contrastFilter.setContrast(amt);
    addEffect(contrastFilter, image);
  }

}