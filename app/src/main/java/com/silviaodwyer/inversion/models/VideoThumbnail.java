package com.silviaodwyer.inversion.models;

import android.graphics.Bitmap;
import com.daasuu.gpuv.egl.filter.GlFilter;

public class VideoThumbnail {

    private Bitmap bitmap;
    private String filterName;
    private GlFilter filter;

    public VideoThumbnail(Bitmap bitmap, String filterName, GlFilter filter) {
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

    public GlFilter getFilter() {
        return filter;
    }
}
