package com.silviaodwyer.inversion.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bumptech.glide.signature.ObjectKey;
import com.daasuu.gpuv.egl.filter.*;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.silviaodwyer.inversion.ImageFilters;
import com.silviaodwyer.inversion.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter;

public class VideoFilters {
  private ArrayList<GlFilter> videoFilters = new ArrayList<>();
  private Context context;
  private List<List<Object>> effectFilters;

  public VideoFilters(Context context) {
    this.context = context;
    effectFilters = new ArrayList<>();

    this.addEffectFilters();
  }

  public void addEffectFilters() {
    effectFilters.add(Arrays.asList("Sepia", new GlSepiaFilter()));
    effectFilters.add(Arrays.asList("Grayscale", new GlGrayScaleFilter()));
    effectFilters.add(Arrays.asList("Vignette", new GlVignetteFilter()));
    effectFilters.add(Arrays.asList("Sepia Vignette", sepiaVignetteFilter()));
    effectFilters.add(Arrays.asList("Rubrik", rubrikFilter()));
    effectFilters.add(Arrays.asList("Neue", neueFilter()));
    effectFilters.add(Arrays.asList("Vignette Grayscale", vignetteGrayscaleFilter()));

    for (List filter: effectFilters) {
      videoFilters.add((GlFilter) filter.get(1));
    }
  }

  public void addBlendFilters() {
    List<Integer> bitmaps = Arrays.asList(R.mipmap.galaxy, R.mipmap.rainbow,
            R.mipmap.cosmic, R.mipmap.pink);

    for (Integer bitmapResource: bitmaps) {
      Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResource);

      this.videoFilters.add(new GLSoftLightBlendFilter(bitmap));
      this.videoFilters.add(new GLDarkenBlendFilter(bitmap));
      this.videoFilters.add(new GLMultiplyBlendFilter(bitmap));
    }

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

  public List<List<Object>> getEffectFilters() {
    return effectFilters;
  }

  public ArrayList<GPUImageFilter> getRequiredImageFilters() {
    ImageFilters imageFilters = new ImageFilters(context);
    List<List<Object>> allImageFilters = imageFilters.getEffectFilters();
    ArrayList<GPUImageFilter> requiredImageFilters = new ArrayList<>();

    for (List<Object> filter: allImageFilters) {
      if (imageFilters.getFilterFromName( (String) filter.get(0)) != null) {
          requiredImageFilters.add((GPUImageFilter) filter.get(1));
      }
    }

    return requiredImageFilters;

  }

}
