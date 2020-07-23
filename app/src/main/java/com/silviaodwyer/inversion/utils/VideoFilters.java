package com.silviaodwyer.inversion.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.daasuu.gpuv.egl.filter.*;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.silviaodwyer.inversion.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoFilters {
  private ArrayList<GlFilter> videoFilters = new ArrayList<>();
  private Context context;

  public VideoFilters(Context context) {
    this.context = context;
    this.addBlendFilters();
    this.videoFilters.add(new GlSepiaFilter());
    this.videoFilters.add(new GlSaturationFilter());
    this.videoFilters.add(new GlLuminanceFilter());
    this.videoFilters.add(new GlMonochromeFilter());
    this.videoFilters.add(new GlBrightnessFilter());
    this.videoFilters.add(new GlGrayScaleFilter());
    this.videoFilters.add(new GlVignetteFilter());
    this.videoFilters.add(vignetteGrayscaleFilter());
    this.videoFilters.add(sepiaVignetteFilter());
    this.videoFilters.add(rubrikFilter());
    this.videoFilters.add(neueFilter());
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

  public void setVideoFilters(ArrayList<GlFilter> videoFilters) {
    this.videoFilters = videoFilters;
  }

//  public ArrayList<Bitmap> generateThumbnails() {
//
//  }

  public GlFilterGroup vignetteGrayscaleFilter() {
    GlFilterGroup filterGroup = new GlFilterGroup(new GlGrayScaleFilter(), new GlVignetteFilter());
    return filterGroup;
  }

  public GlFilterGroup sepiaVignetteFilter() {
    GlFilterGroup filterGroup = new GlFilterGroup(new GlSepiaFilter(), new GlVignetteFilter());
    return filterGroup;
  }

  public GlFilterGroup rubrikFilter() {
    GlFilter brightnessFilter = new GlBrightnessFilter();
    ((GlBrightnessFilter) brightnessFilter).setBrightness((float) 0.5);
    GlFilter contrastFilter = new GlContrastFilter();
    ((GlContrastFilter) contrastFilter).setContrast((float) 1.2);

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

}
