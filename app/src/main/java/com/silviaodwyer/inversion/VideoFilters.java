package com.silviaodwyer.inversion;

import android.content.Context;
import android.graphics.Bitmap;

import com.daasuu.epf.EPlayerView;
import com.daasuu.epf.filter.*;

import java.util.ArrayList;

public class VideoFilters {
  private ArrayList<GlFilter> videoFilters = new ArrayList<>();

  public VideoFilters() {
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


  /**
   * Filter the video by applying the filter passed.
   **/
  public void filterVideo(GlFilter filter, EPlayerView ePlayerView) {
    ePlayerView.setGlFilter(filter);
  }

}
