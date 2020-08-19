package com.silviaodwyer.inversion;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLuminosityBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSoftLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter;

public class GradientFilters extends ImageFilters {
    private List<Integer> gradient_backgrounds;
    private List<List<Object>> filtersWithNames;
    private ArrayList<GPUImageFilter> filters;
    private ArrayList<GPUImageFilter> gradient_twoblend_filters;

    public GradientFilters(Context context) {
        super(context);
        gradient_backgrounds = Arrays.asList(R.mipmap.gradient2, R.mipmap.gradient3, R.mipmap.summer, R.mipmap.atlantic, R.mipmap.cosmic,
                R.mipmap.lavender, R.mipmap.pink, R.mipmap.purple, R.mipmap.rainbow, R.mipmap.stars,
                R.mipmap.overlay1, R.mipmap.overlay2, R.mipmap.vintage1,
                R.mipmap.vintage2, R.mipmap.vintage3, R.mipmap.vintage4);
        filtersWithNames = new ArrayList<>();
        filters = new ArrayList<>();
        gradient_twoblend_filters = new ArrayList<>();
    }

    public List<List<Object>> createGradientFilters() {
        List<List<Object>> gradientFiltersWithNames = new ArrayList<>();

        for (int k = 0; k < gradient_backgrounds.size(); k++) {
            int background = gradient_backgrounds.get(k);
            Bitmap bmp = decodeBitmapFromResource(background);

            GPUImageFilter filter = createTwoBlendFilter(super.getContext(), GPUImageSoftLightBlendFilter.class, bmp);
            gradientFiltersWithNames.add(Arrays.asList("Gradient " + k, filter));
        }

        return gradientFiltersWithNames;
    }

    public List<List<Object>> createGradientGrayscaleFilters() {
        List<List<Object>> gradientGrayscaleFiltersWithNames = new ArrayList<>();
        gradientGrayscaleFiltersWithNames.add(Arrays.asList("SepiaColorBlend", createSepiaColorBlendFilter()));

        for (int j = 0; j < gradient_backgrounds.size(); j++) {
            GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
            Bitmap bmp = decodeBitmapFromResource(gradient_backgrounds.get(j));

            GPUImageFilter filter =  createTwoBlendFilter(super.getContext(), GPUImageSoftLightBlendFilter.class, bmp);

            filterGroup.addFilter(getObsidianFilter());
            filterGroup.addFilter(filter);
            gradientGrayscaleFiltersWithNames.add(Arrays.asList("Gradient BW " + j, filterGroup));
        }
        return gradientGrayscaleFiltersWithNames;
    }

    public  GPUImageFilterGroup createSepiaColorBlendFilter() {
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        Bitmap bmp = decodeBitmapFromResource(gradient_backgrounds.get(4));

        GPUImageFilter filter =  createTwoBlendFilter(super.getContext(), GPUImageColorBlendFilter.class, bmp);

        filterGroup.addFilter(filter);
        filterGroup.addFilter(new GPUImageSepiaToneFilter());
        return filterGroup;
    }

    public List<List<Object>> createGradientDissolveFilters() {
        List<List<Object>> gradientFiltersWithNames = new ArrayList<>();
        for (int k = 0; k < gradient_backgrounds.size(); k++) {
            int background = gradient_backgrounds.get(k);
            Bitmap bmp = decodeBitmapFromResource(background);

            GPUImageFilter filter = createTwoBlendFilter(super.getContext(), GPUImageDissolveBlendFilter.class, bmp);

            gradientFiltersWithNames.add(Arrays.asList("Gradient " + k, filter));
        }

        return gradientFiltersWithNames;
    }

    public List<List<Object>> createGradientColorBlendFilters() {
        List<List<Object>> gradientFiltersWithNames = new ArrayList<>();

        for (int k = 0; k < gradient_backgrounds.size(); k++) {
            int background = gradient_backgrounds.get(k);
            Bitmap bmp = decodeBitmapFromResource(background);

            GPUImageFilter filter = createTwoBlendFilter(super.getContext(), GPUImageColorBlendFilter.class, bmp);
            gradientFiltersWithNames.add(Arrays.asList("Gradient " + k, filter));
        }
        return gradientFiltersWithNames;
    }

    public List<List<Object>> getFiltersWithNames() {
        return filtersWithNames;
    }

    @Override
    public ArrayList<GPUImageFilter> getFilters() {
        return filters;
    }

}
