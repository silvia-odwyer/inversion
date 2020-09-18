package com.silviaodwyer.inversion.models;

import android.graphics.Bitmap;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class ImageThumbnail {

    private Bitmap bitmap;
    private String filterName;
    private GPUImageFilter filter;

    public ImageThumbnail(Bitmap bitmap, String filterName, GPUImageFilter filter) {
        this.bitmap = bitmap;
        this.filterName = filterName;
        this.filter = filter;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getFilterName() {
        return filterName;
    }

    public GPUImageFilter getFilter() {
        return filter;
    }
}
