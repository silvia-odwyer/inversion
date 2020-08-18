package com.silviaodwyer.inversion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter;

public class BlendEffectFilters extends ImageFilters {
    private Bitmap originalBitmap;

    public BlendEffectFilters(Context context, Bitmap originalBitmap) {
        super(context);
        this.originalBitmap = originalBitmap;
    }

    public List<List<Object>> createGlitchEffectFilters() {

        List<List<Object>> allfilters = new ArrayList<>();
        allfilters.add(Arrays.asList("Infra", getInfraFilter()));
        allfilters.add(Arrays.asList("Lithic", getLithicFilter()));
        allfilters.add(Arrays.asList("Solarize", new GPUImageSolarizeFilter()));

        return allfilters;
    }

    public GPUImageFilter getInfraFilter() {

        // invert image
        GPUImage gpuImage = new GPUImage(getContext());
        gpuImage.setImage(originalBitmap);
        gpuImage.setFilter(new GPUImageColorInvertFilter());
        Bitmap invertedImage = gpuImage.getBitmapWithFilterApplied();

        // blend (color mode) inverted image with original bitmap
        GPUImageFilter filter = createTwoBlendFilter(getContext(), GPUImageColorBlendFilter.class, invertedImage);

        return filter;
    }

    public GPUImageFilter getLithicFilter() {

        // invert image
        GPUImage gpuImage = new GPUImage(getContext());
        gpuImage.setImage(originalBitmap);
        GPUImageFilterGroup fg = new GPUImageFilterGroup();
        fg.addFilter(new GPUImageColorInvertFilter());
        fg.addFilter(new GPUImageHueFilter(60));
        fg.addFilter(new GPUImageSaturationFilter((float) 0.45));

        gpuImage.setFilter(fg);
        Bitmap invertedImage = gpuImage.getBitmapWithFilterApplied();

        // blend (color mode) inverted image with original bitmap
        GPUImageFilter filter = createTwoBlendFilter(getContext(), GPUImageColorBlendFilter.class, invertedImage);


        return filter;
    }



}
