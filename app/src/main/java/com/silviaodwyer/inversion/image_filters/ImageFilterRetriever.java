package com.silviaodwyer.inversion.image_filters;

import android.content.ContentResolver;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVibranceFilter;

public class ImageFilterRetriever {
    private Context context;
    private ImageFilters imageFilters;
    private GradientFilters gradientFilters;

    public ImageFilterRetriever(Context context) {
        this.context = context;
        this.imageFilters = new ImageFilters(context);
        this.gradientFilters = new GradientFilters(context);
    }

    public GPUImageFilter retrieveFilter(String filterName) {
        GPUImageFilter filter = null;
        String category = ImageFilters.filterInCategory(filterName);

        if (category != null) {
            String index_str = filterName.replace(category, "").trim();
            int index = Integer.parseInt(index_str);

            List<List<Object>> filters = null;
            switch (category) {
                case "Gradient":
                    filters = gradientFilters.getGradientFilters();
                    break;
                case "BWGradient":
                    filters = gradientFilters.createGradientGrayscaleFilters();
                    break;
                case "Color Blend":
                    filters = gradientFilters.getColorBlendFilters();
            }
            filter = (GPUImageFilter) filters.get(index).get(1);
        }
        else {
            filter = getFilterFromName(filterName);
        }
        return filter;
    }

    public GPUImageFilter getFilterFromName(String filterName) {
        switch (filterName) {
            case "Neon Pink":
                return imageFilters.getNeonPinkFilter();
            case "Eight":
                return imageFilters.getEightFilter();
            case "Orbiton":
                return imageFilters.getOribitonFilter();
            case "Aesthetica":
                return imageFilters.getAestheticaFilter();
            case "Sepia":
                return new GPUImageSepiaToneFilter();
            case "Grayscale":
                return new GPUImageGrayscaleFilter();
            case "Vivid":
                return imageFilters.getVividFilter();
            case "Vibrance":
                return new GPUImageVibranceFilter();
            case "Dramatic":
                return imageFilters.getDramaticFilter();
            case "Obsidian":
                return imageFilters.getObsidianFilter();
            case "Vibrancy":
                return imageFilters.getVibrancyFilter();
            case "Rubrik":
                return imageFilters.getRubrikFilter();
            case "Cali":
                return imageFilters.getCaliFilter();
            case "Retro 1":
                return imageFilters.getRetroFilter();
            case "Retro2":
                return imageFilters.getRetroFilter2();
            case "Retro3":
                return imageFilters.getRetroFilter3();
            case "Retro Vignette":
                return imageFilters.getRetroVignette();
            case "Retro Vignette 2":
                return imageFilters.getRetroVignette2();
            case "Lomo":
                return imageFilters.getLomoFilter();
            default:
                Toast.makeText(context, "Filter: " + filterName + "not found", Toast.LENGTH_SHORT).show();
                return imageFilters.getLomoFilter();
        }
    }
}
