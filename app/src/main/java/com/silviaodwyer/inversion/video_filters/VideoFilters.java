package com.silviaodwyer.inversion.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.daasuu.gpuv.egl.filter.*;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.silviaodwyer.inversion.image_filters.GradientFilters;
import com.silviaodwyer.inversion.image_filters.ImageFilterPacks;
import com.silviaodwyer.inversion.ImageUtils;
import com.silviaodwyer.inversion.VideoThumbnail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class VideoFilters {
  private ArrayList<GlFilter> videoFilters = new ArrayList<>();
  private Context context;
  private ImageFilterPacks imageFilterPacks;
  private List<Integer> bitmaps;

  public VideoFilters(Context context) {
    this.context = context;
    imageFilterPacks = new ImageFilterPacks(context);
    bitmaps = GradientFilters.getBlendBackgrounds();

  }

  public List<List<Object>> createEffectFilters() {
    List<List<Object>> effectFilters = new ArrayList<>();
    effectFilters.add(Arrays.asList("Grayscale", new GlGrayScaleFilter()));
    effectFilters.add(Arrays.asList("Vignette", new GlVignetteFilter()));
    effectFilters.add(Arrays.asList("Sepia Vignette", sepiaVignetteFilter()));
    effectFilters.add(Arrays.asList("Rubrik", rubrikFilter()));
    effectFilters.add(Arrays.asList("Neue", neueFilter()));

    return effectFilters;
  }

  public List<List<Object>> createVintageFilters() {
    List<List<Object>> vintageFilters = new ArrayList<>();
    vintageFilters.add(Arrays.asList("Sepia", new GlSepiaFilter()));
    vintageFilters.add(Arrays.asList("Vignette Grayscale", vignetteGrayscaleFilter()));

    return vintageFilters;
  }

    public List<List<Object>> createBlendFilters() {
        List<List<Object>> blendFilters = new ArrayList<>();
        for (int k = 0; k < bitmaps.size(); k++) {
            Integer bitmapResource = bitmaps.get(k);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResource);
            blendFilters.add(Arrays.asList("Gradient " + k, new GLSoftLightBlendFilter(bitmap)));

        }
        return blendFilters;
    }

    public List<List<Object>> createColorBlendFilters() {
        List<List<Object>> blendFilters = new ArrayList<>();
        for (int k = 0; k < bitmaps.size(); k++) {
            Integer bitmapResource = bitmaps.get(k);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResource);
            blendFilters.add(Arrays.asList("Color Blend " + k, new GLMultiplyBlendFilter(bitmap)));

        }
        return blendFilters;
    }

  public ArrayList<GlFilter> getVideoFilters() {
    return videoFilters;
  }

  public GlFilterGroup vignetteGrayscaleFilter() {
    return new GlFilterGroup(new GlGrayScaleFilter(), new GlVignetteFilter());
  }

  public GlFilterGroup sepiaVignetteFilter() {
    return new GlFilterGroup(new GlSepiaFilter(), new GlVignetteFilter());
  }

  public GlFilterGroup rubrikFilter() {
    GlBrightnessFilter brightnessFilter = new GlBrightnessFilter();
    brightnessFilter.setBrightness((float) 0.5);
    GlContrastFilter contrastFilter = new GlContrastFilter();
    contrastFilter.setContrast((float) 1.2);

    GlFilterGroup rubrikFilter = new GlFilterGroup(new GlContrastFilter(), brightnessFilter );
    return rubrikFilter;
  }

  public GlFilterGroup neueFilter() {
    GlFilter bilateralFilter = new GlBilateralFilter();
    GlFilterGroup filterGroup = new GlFilterGroup(bilateralFilter);
    return filterGroup;
  }

  /**
   * Filter the video by applying the filter passed.
   **/
  public void filterVideo(GlFilter filter, GPUPlayerView ePlayerView) {
    ePlayerView.setGlFilter(filter);

  }

  public ArrayList<GPUImageFilter> getRequiredImageFilters( List<List<Object>> videoFilters) {
    ArrayList<GPUImageFilter> requiredImageFilters = new ArrayList<>();

    for (List<Object> filter: videoFilters) {
      GPUImageFilter correspondingImageFilter = imageFilterPacks.retrieveFilter(((String) filter.get(0)));
      if (correspondingImageFilter != null) {
          requiredImageFilters.add(correspondingImageFilter);
      }
    }

    return requiredImageFilters;

  }

  public ArrayList<VideoThumbnail> getFilteredThumbnails(Bitmap bitmap, List<List<Object>> filtersWithNames) {
    ArrayList<GlFilter> filters = convertToFilters(filtersWithNames);
    ArrayList<VideoThumbnail> imageThumbnails = new ArrayList<>();
    ArrayList<GPUImageFilter> correspondingImageFilters = getRequiredImageFilters(filtersWithNames);
    ArrayList<Bitmap> thumbnailBitmaps = ImageUtils.generateThumbnailBitmaps(bitmap, correspondingImageFilters);
    for (int index = 0; index < thumbnailBitmaps.size(); index++) {
      Bitmap thumbnailBitmap = thumbnailBitmaps.get(index);
      VideoThumbnail imageThumbnail = new VideoThumbnail(thumbnailBitmap, (String) filtersWithNames.get(index).get(0), filters.get(index));
      imageThumbnails.add(imageThumbnail);
    }
    return imageThumbnails;
  }

  public ArrayList<GlFilter> convertToFilters(List<List<Object>> filtersWithNames) {
    ArrayList<GlFilter> filters = new ArrayList<>();
    for (List<Object> filterList: filtersWithNames) {
      filters.add((GlFilter) filterList.get(1));
    }
    return filters;
  }


}
