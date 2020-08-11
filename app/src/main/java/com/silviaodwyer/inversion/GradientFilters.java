package com.silviaodwyer.inversion;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSoftLightBlendFilter;

public class GradientFilters extends ImageFilters {
    private List<Integer> gradient_backgrounds;
    private Context context;
    private List<List<Object>> filtersWithNames;
    private ArrayList<GPUImageFilter> filters;
    private ArrayList<GPUImageFilter> gradient_twoblend_filters;

    public GradientFilters(Context context) {
        super(context);
        this.context = context;
        gradient_backgrounds = Arrays.asList(R.mipmap.summer, R.mipmap.atlantic, R.mipmap.cosmic,
                R.mipmap.lavender, R.mipmap.pink, R.mipmap.purple, R.mipmap.rainbow, R.mipmap.stars,
                R.mipmap.overlay1, R.mipmap.overlay2, R.mipmap.vintage1,
                R.mipmap.vintage2, R.mipmap.vintage3, R.mipmap.vintage4);
        filtersWithNames = new ArrayList<>();
        filters = new ArrayList<>();
        gradient_twoblend_filters = new ArrayList<>();
        this.initGradientTwoBlendFilters();
    }

    public void initGradientTwoBlendFilters() {
        for (int k = 0; k < gradient_backgrounds.size(); k++) {
            int background = gradient_backgrounds.get(k);
            GPUImageFilter filter = createTwoBlendFilter(context, GPUImageSoftLightBlendFilter.class, background);
            gradient_twoblend_filters.add(filter);
        }
    }

    public List<List<Object>> createGradientFilters() {
        List<List<Object>> gradientFiltersWithNames = new ArrayList<>();
        for (int k = 0; k < gradient_backgrounds.size(); k++) {
            GPUImageFilter filter = gradient_twoblend_filters.get(k);
            gradientFiltersWithNames.add(Arrays.asList("Gradient " + k, filter));
        }
        return gradientFiltersWithNames;
    }

    public List<List<Object>> createGradientGrayscaleFilters() {
        List<List<Object>> gradientGrayscaleFiltersWithNames = new ArrayList<>();

        for (int j = 0; j < gradient_backgrounds.size(); j++) {
            GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
            filterGroup.addFilter(getObsidianFilter());
            GPUImageFilter filter = gradient_twoblend_filters.get(j);
            filterGroup.addFilter(filter);
            gradientGrayscaleFiltersWithNames.add(Arrays.asList("Gradient BW " + j, filterGroup));
        }
        return gradientGrayscaleFiltersWithNames;
    }

    public List<List<Object>> getFiltersWithNames() {
        return filtersWithNames;
    }

    @Override
    public ArrayList<GPUImageFilter> getFilters() {
        return filters;
    }

}
