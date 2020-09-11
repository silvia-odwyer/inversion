package com.silviaodwyer.inversion.image_filters;

import android.content.Context;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class ImageFilterPacks {
    private ImageFilters imageFilters;
    private GradientFilters gradientFilters;

    public ImageFilterPacks(Context context) {
        imageFilters = new ImageFilters(context);
        gradientFilters = new GradientFilters(context);
    }

    public GPUImageFilter retrieveFilter(String filterName) {
        GPUImageFilter filter = null;
        String category = imageFilters.filterInCategory(filterName);

        if (category != null) {
            String index_str = filterName.replace(category, "").trim();
            int index = Integer.parseInt(index_str);

            List<List<Object>> filters = null;
            switch (category) {
                case "Gradient":
                    filters = gradientFilters.getGradientFilters();
                    break;
                case "Color Blend":
                    filters = gradientFilters.getColorBlendFilters();
            }
            filter = (GPUImageFilter) filters.get(index).get(1);
        }
        else {
            imageFilters.getFilterFromName(filterName);
        }
        return filter;
    }
}
