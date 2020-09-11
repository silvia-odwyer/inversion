package com.silviaodwyer.inversion;

import android.content.Context;
import android.graphics.Bitmap;

import com.silviaodwyer.inversion.image_filters.ImageFilters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter;

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
        allfilters.add(Arrays.asList("Solarize Two", getSolarizeTwoFilter()));

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
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        filterGroup.addFilter(new GPUImageColorInvertFilter());
        filterGroup.addFilter(new GPUImageHueFilter(60));
        filterGroup.addFilter(new GPUImageSaturationFilter((float) 0.45));

        gpuImage.setFilter(filterGroup);
        Bitmap invertedImage = gpuImage.getBitmapWithFilterApplied();

        // blend (color mode) inverted image with original bitmap
        GPUImageFilter filter = createTwoBlendFilter(getContext(), GPUImageColorBlendFilter.class, invertedImage);


        return filter;
    }

    public GPUImageFilter getSolarizeTwoFilter() {
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        filterGroup.addFilter(new GPUImageSolarizeFilter(2));
        filterGroup.addFilter(new GPUImageHueFilter(60));

        return filterGroup;
    }

}
