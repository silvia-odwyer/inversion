package com.silviaodwyer.inversion;

import android.graphics.Bitmap;

import com.daasuu.epf.filter.GlFilter;
import com.daasuu.epf.filter.GlGrayScaleFilter;
import com.daasuu.epf.filter.GlSepiaFilter;
import com.daasuu.epf.filter.GlVignetteFilter;

import java.util.ArrayList;

public class VideoFilters {
  private ArrayList<GlFilter> videoFilters;

  public VideoFilters() {
    this.videoFilters.add(new GlSepiaFilter());
    this.videoFilters.add(new GlGrayScaleFilter());
    this.videoFilters.add(new GlVignetteFilter());
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


}
