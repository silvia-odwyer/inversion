package com.silviaodwyer.inversion.image_filters;

import android.content.Context;
import android.graphics.Bitmap;

import com.silviaodwyer.inversion.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSoftLightBlendFilter;

public class GradientFilters extends ImageFilters {
    private static List<Integer> gradient_backgrounds = Arrays.asList(R.mipmap.gradient2, R.mipmap.gradient3, R.mipmap.summer, R.mipmap.atlantic, R.mipmap.cosmic,
            R.mipmap.lavender, R.mipmap.pink, R.mipmap.purple, R.mipmap.rainbow, R.mipmap.stars,
            R.mipmap.overlay1, R.mipmap.overlay2, R.mipmap.vintage1,
            R.mipmap.vintage2, R.mipmap.vintage3, R.mipmap.vintage4);
    private List<List<Object>> filtersWithNames;
    private ArrayList<GPUImageFilter> filters;
    private ArrayList<GPUImageFilter> gradient_twoblend_filters;
    private List<List<Object>> gradientFiltersWithNames;
    private List<List<Object>> colorBlendFilters;

    public GradientFilters(Context context) {
        super(context);
        filtersWithNames = new ArrayList<>();
        filters = new ArrayList<>();
        colorBlendFilters = new ArrayList<>();
        gradient_twoblend_filters = new ArrayList<>();
        gradientFiltersWithNames = new ArrayList<>();

    }

    public static List<Integer> getBlendBackgrounds() {
        return gradient_backgrounds;
    }

    public List<List<Object>> createGradientFilters() {
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

        for (int k = 0; k < gradient_backgrounds.size(); k++) {
            int background = gradient_backgrounds.get(k);
            Bitmap bmp = decodeBitmapFromResource(background);

            GPUImageFilter filter = createTwoBlendFilter(super.getContext(), GPUImageColorBlendFilter.class, bmp);
            colorBlendFilters.add(Arrays.asList("Color Blend " + k, filter));
        }
        return colorBlendFilters;
    }

    List<List<Object>> getGradientFilters() {
        if (gradientFiltersWithNames.isEmpty()) {
            createGradientFilters();
        }
        return gradientFiltersWithNames;
    }

    public List<List<Object>> getColorBlendFilters() {
        if (colorBlendFilters.isEmpty()) {
            colorBlendFilters = createGradientColorBlendFilters();
        }
        return colorBlendFilters;
    }

    public List<List<Object>> getFiltersWithNames() {
        return filtersWithNames;
    }

    @Override
    public ArrayList<GPUImageFilter> getFilters() {
        return filters;
    }

}
