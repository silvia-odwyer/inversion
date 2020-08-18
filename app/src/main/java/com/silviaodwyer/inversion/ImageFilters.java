package com.silviaodwyer.inversion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.*;

public class ImageFilters {
  private ArrayList<GPUImageFilter> filters;
  private ArrayList<GPUImageFilter> correctionFilters;
  private GPUImageFilterGroup activeFilterGroup = new GPUImageFilterGroup();
  private Context context;
  private GPUImageBrightnessFilter brightnessFilter;
  private GPUImageContrastFilter contrastFilter;
  private GPUImageSaturationFilter saturationFilter;
  private List<List<Object>> effectFilters;
  private Bitmap bitmap;

  public enum FilterType {
    EFFECT,
    CORRECTION,
  }

  public ImageFilters(Context context) {
    this.context = context;
    filters = new ArrayList<>();
    this.bitmap = bitmap;
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

  public static GPUImageFilter createTwoBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> twoInputFilterClass,
                                                     Bitmap bitmap) {
    try {
      // create a two input filter
      GPUImageTwoInputFilter filter = twoInputFilterClass.newInstance();

      // set bitmap to blend with
      filter.setBitmap(bitmap);
      return filter;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Bitmap decodeBitmapFromResource(int resource_id) {
    return BitmapFactory.decodeResource(context.getResources(), resource_id);
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

  public List<List<Object>> createVintageFilters() {

    List<List<Object>> allfilters = new ArrayList<>();
    allfilters.add(Arrays.asList("Dramatic", getDramaticFilter()));
    allfilters.add(Arrays.asList("Sepia", new GPUImageSepiaToneFilter()));
    allfilters.add(Arrays.asList("Obsidian", getObsidianFilter()));
    allfilters.add(Arrays.asList("Grayscale", new GPUImageGrayscaleFilter()));
    allfilters.add(Arrays.asList("Retro 1", getRetroFilter()));
    allfilters.add(Arrays.asList("Retro 2", getRetroFilter2()));
    allfilters.add(Arrays.asList("Vivid", getVividFilter()));
    allfilters.add(Arrays.asList("Retro Vignette", getRetroVignette()));
    allfilters.add(Arrays.asList("Retro Vignette 2", getRetroVignette2()));
    allfilters.add(Arrays.asList("Lomo", getLomoFilter()));
    return allfilters;
  }

  public GPUImageFilterGroup getVividFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(new GPUImageSaturationFilter((float) 1.5));
    filterGroup.addFilter(new GPUImageContrastFilter((float) 1.5));
    filterGroup.addFilter(new GPUImageHueFilter(-15));

    return filterGroup;
  }

  private GPUImageFilterGroup getOribitonFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(getObsidianFilter());
    Bitmap bmp = decodeBitmapFromResource(R.mipmap.purple);
    GPUImageFilter filter = createTwoBlendFilter(context, GPUImageSoftLightBlendFilter.class, bmp);
    filterGroup.addFilter(filter);
    return filterGroup;
  }

  private GPUImageFilterGroup getNeonPinkFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(getObsidianFilter());
    Bitmap bmp = decodeBitmapFromResource(R.mipmap.pink);

    GPUImageFilter filter = createTwoBlendFilter(context, GPUImageSoftLightBlendFilter.class, bmp);
    filterGroup.addFilter(filter);
    return filterGroup;
  }

  public GPUImageFilterGroup getDramaticFilter() {
    float amount = (float) 1.1;
    float amount2 = (float) 0.5;

    GPUImageFilterGroup dramaticFilter = new GPUImageFilterGroup();
    dramaticFilter.addFilter(new GPUImageGrayscaleFilter());
    dramaticFilter.addFilter(new GPUImageContrastFilter(amount));
    return dramaticFilter;
  }

  public GPUImageFilterGroup getRubrikFilter() {
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

  private GPUImageFilterGroup getLomoFilter() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(getVividFilter());
    filterGroup.addFilter(createVignette());

    return filterGroup;
  }

  private GPUImageVignetteFilter createVignette() {
    float vignetteStart = 0.3f;
    float vignetteEnd = 0.78f;

    GPUImageVignetteFilter vf = new GPUImageVignetteFilter(new PointF(0.5f, 0.5f),
            new float[]{0.0f, 0.0f, 0.0f},
            vignetteStart,
            vignetteEnd);

    return vf;
  }

  private GPUImageFilterGroup getRetroVignette() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(getRetroFilter());
    filterGroup.addFilter(createVignette());

    return filterGroup;
  }

  public GPUImageFilterGroup getRetroVignette2() {
    GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
    filterGroup.addFilter(getRetroFilter2());
    filterGroup.addFilter(createVignette());
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

  public ArrayList<ImageThumbnail> getFilteredThumbnails(Bitmap bitmap, List<List<Object>> filtersWithNames) {
        ArrayList<GPUImageFilter> filters = convertToFilters(filtersWithNames);
        ArrayList<ImageThumbnail> imageThumbnails = new ArrayList<>();
        ArrayList<Bitmap> thumbnailBitmaps = ImageUtils.generateThumbnailBitmaps(bitmap, filters);
        for (int index = 0; index < thumbnailBitmaps.size(); index++) {
            Bitmap thumbnailBitmap = thumbnailBitmaps.get(index);
            ImageThumbnail imageThumbnail = new ImageThumbnail(thumbnailBitmap, (String) filtersWithNames.get(index).get(0), filters.get(index));
            imageThumbnails.add(imageThumbnail);
        }
        return imageThumbnails;
    }

    public ArrayList<GPUImageFilter> convertToFilters(List<List<Object>> filtersWithNames) {
      ArrayList<GPUImageFilter> filters = new ArrayList<>();
      for (List<Object> filterList: filtersWithNames) {
          filters.add((GPUImageFilter) filterList.get(1));
      }
      return filters;
    }


    public void filterImageFromName(String filterName, Image image) {
    GPUImageFilter filter = getFilterFromName(filterName);
    image.getMetaData().setAppliedFilter(filterName);

    ImageEditor activity = image.getActivity();
    activity.updateGPUImage(filter);
  }

  public String filterInCategory(String filterName) {
    String[] categories = {"Gradient", "Color Blend"};
    for (String category: categories) {
      if (filterName.startsWith(category)) {
        return category;
      }
    }
    return null;
  }

  public GPUImageFilter getFilterFromName(String filterName) {
    GPUImageFilter filter = null;
    String category = filterInCategory(filterName);

    if (category != null) {
      String index_str = filterName.replace(category, "").trim();
      Log.d("DEBUG", "INDEX STR: " + index_str);
    }
    else {

      switch (filterName) {
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
    }
    return filter;
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

  public Context getContext() {
    return context;
  }


}