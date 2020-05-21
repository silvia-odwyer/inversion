package com.silviaodwyer.inversion;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;

public class ImageFilters {
  private ArrayList<GPUImageFilter> filters;

  public ImageFilters() {
    filters = new ArrayList<GPUImageFilter>();
    filters.add(new GPUImageSepiaToneFilter());
    filters.add(new GPUImageToonFilter());
  }

  public ArrayList<GPUImageFilter> getFilters() {
    return filters;
  }

  public void setFilters(ArrayList<GPUImageFilter> filters) {
    this.filters = filters;
  }


}
