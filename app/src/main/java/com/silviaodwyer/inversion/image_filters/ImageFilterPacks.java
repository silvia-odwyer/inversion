package com.silviaodwyer.inversion.image_filters;

import android.content.Context;
import android.graphics.Bitmap;

import com.silviaodwyer.inversion.image_filters.glitch_filters.BGRSwitchFilter;
import com.silviaodwyer.inversion.image_filters.glitch_filters.BRGSwitchFilter;
import com.silviaodwyer.inversion.image_filters.glitch_filters.RBGSwitchFilter;
import com.silviaodwyer.inversion.image_filters.glitch_filters.RBRSwitchFilter;
import com.silviaodwyer.inversion.image_filters.glitch_filters.TintFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFalseColorFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHalftoneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageRGBDilationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSmoothToonFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVibranceFilter;

public class ImageFilterPacks {
    private ImageFilters imageFilters;
    private GradientFilters gradientFilters;
    private BlendEffectFilters blendEffectFilters;
    private ImageFilterRetriever imageFilterRetriever;

    public ImageFilterPacks(Context context, Bitmap originalBitmap) {
        imageFilters = new ImageFilters(context);
        gradientFilters = new GradientFilters(context);
        imageFilterRetriever = new ImageFilterRetriever(context);
        blendEffectFilters = new BlendEffectFilters(context, originalBitmap);
    }

    public List<List<Object>> createVintageFilters() {

        List<List<Object>> allfilters = new ArrayList<>();
        allfilters.add(Arrays.asList("Dramatic", imageFilters.getDramaticFilter()));
        allfilters.add(Arrays.asList("Sepia", new GPUImageSepiaToneFilter()));
        allfilters.add(Arrays.asList("Obsidian", imageFilters.getObsidianFilter()));
        allfilters.add(Arrays.asList("Grayscale", new GPUImageGrayscaleFilter()));
        allfilters.add(Arrays.asList("Grayscale Vignette", imageFilters.getGrayscaleVignette()));
        allfilters.add(Arrays.asList("Grayscale Vignette 2", imageFilters.getGrayscaleVignette2()));

        return allfilters;
    }

    public List<List<Object>> createRetroFilters() {

        List<List<Object>> allfilters = new ArrayList<>();
        allfilters.add(Arrays.asList("Retro 1", imageFilters.getRetroFilter()));
        allfilters.add(Arrays.asList("Retro 2", imageFilters.getRetroFilter2()));
        allfilters.add(Arrays.asList("Vivid", imageFilters.getVividFilter()));
        allfilters.add(Arrays.asList("Retro Vignette", imageFilters.getRetroVignette()));
        allfilters.add(Arrays.asList("Retro Vignette 2", imageFilters.getRetroVignette2()));
        allfilters.add(Arrays.asList("Lomo", imageFilters.getLomoFilter()));
        allfilters.add(Arrays.asList("Lomo Fuschia", imageFilters.getLomoFuschiaFilter()));
        allfilters.add(Arrays.asList("Lomo Violet", imageFilters.getLomoVioletFilter()));
        allfilters.add(Arrays.asList("Lomo Blue", imageFilters.getLomoBlueFilter()));

        return allfilters;
    }

    public List<List<Object>> createPopularFilters() {

        List<List<Object>> allfilters = new ArrayList<>();
        allfilters.add(Arrays.asList("Infra", blendEffectFilters.getInfraFilter()));
        allfilters.add(Arrays.asList("Obsidian", imageFilters.getObsidianFilter()));
        allfilters.add(Arrays.asList("Grayscale", new GPUImageGrayscaleFilter()));
        allfilters.add(Arrays.asList("Retro 1", imageFilters.getRetroFilter()));
        allfilters.add(Arrays.asList("Retro 2", imageFilters.getRetroFilter2()));
        allfilters.add(Arrays.asList("Vivid", imageFilters.getVividFilter()));
        allfilters.add(Arrays.asList("Retro Vignette", imageFilters.getRetroVignette()));
        allfilters.add(Arrays.asList("Retro Vignette 2", imageFilters.getRetroVignette2()));
        allfilters.add(Arrays.asList("Lomo", imageFilters.getLomoFilter()));
        allfilters.add(Arrays.asList("Lomo Violet", imageFilters.getLomoVioletFilter()));
        allfilters.add(Arrays.asList("Lomo Blue", imageFilters.getLomoBlueFilter()));
        allfilters.add(Arrays.asList("Lomo Fuschia", imageFilters.getLomoFuschiaFilter()));

        return allfilters;
    }

    public List<List<Object>> createWarmTintFilters() {

        List<List<Object>> allfilters = new ArrayList<>();
        allfilters.add(Arrays.asList("Tint Filter 2", new TintFilter(1.2f, 1.0f, 1.0f)));
        allfilters.add(Arrays.asList("Tint Filter 4", new TintFilter(1.2f, 1.1f, 1.0f)));
        allfilters.add(Arrays.asList("Tint Filter 5", new TintFilter(1.1f, 1.1f, 1.2f)));
        allfilters.add(Arrays.asList("Tint Filter 6", new TintFilter(1.2f, 1.0f, 0.8f)));
        allfilters.add(Arrays.asList("Tint Filter 8", new TintFilter(1.2f, 1.0f, 1.2f)));
        allfilters.add(Arrays.asList("Tint Pink", imageFilters.getPinkTintFilter()));
        allfilters.add(Arrays.asList("Tint Pink", imageFilters.getLafaroFilter()));
        allfilters.add(Arrays.asList("Tint Pink", imageFilters.getLokiFilter()));
        allfilters.add(Arrays.asList("Tint Filter 9", new TintFilter(1.3f, 1.0f, 1.05f)));
        allfilters.add(Arrays.asList("Tint Filter 11", new TintFilter(1.25f, 1.0f, 1.0f)));
        allfilters.add(Arrays.asList("Tint Filter 12", new TintFilter(1.25f, 0.9f, 1.0f)));
        allfilters.add(Arrays.asList("Tint Filter 13", new TintFilter(1.15f, 1.1f, 1.2f)));
        allfilters.add(Arrays.asList("Tint Filter 14", new TintFilter(1.25f, 0.9f, 0.85f)));
        allfilters.add(Arrays.asList("Tint Filter 15", new TintFilter(1.25f, 1.1f, 0.95f)));
        allfilters.add(Arrays.asList("Tint Filter 16", new TintFilter(1.35f, 1.0f, 1.05f)));
        return allfilters;
    }

    public List<List<Object>> createColdTintFilters() {

        List<List<Object>> allfilters = new ArrayList<>();
        allfilters.add(Arrays.asList("Tint Filter 3", new TintFilter(1.0f, 1.1f, 1.2f)));
        allfilters.add(Arrays.asList("Tint Filter 5", new TintFilter(1.1f, 1.1f, 1.2f)));
        allfilters.add(Arrays.asList("Tint Filter 7", new TintFilter(1.0f, 1.08f, 1.4f)));
        allfilters.add(Arrays.asList("Tint Filter 10", new TintFilter(1.1f, 1.05f, 1.4f)));
        allfilters.add(Arrays.asList("Tint Filter 17", new TintFilter(0.95f, 1.1f, 1.2f)));
        allfilters.add(Arrays.asList("Tint Filter 18", new TintFilter(0.8f, 0.9f, 1.1f)));
        allfilters.add(Arrays.asList("Tint Filter 19", new TintFilter(0.97f, 1.02f, 1.25f)));
        allfilters.add(Arrays.asList("Tint Filter 20", new TintFilter(1.1f, 1.05f, 1.4f)));

        return allfilters;
    }



    public List<List<Object>> createColorEffectFilters() {

        List<List<Object>> allfilters = new ArrayList<>();

        allfilters.add(Arrays.asList("BRG Switch", new BRGSwitchFilter()));
        allfilters.add(Arrays.asList("BGR Switch", new BGRSwitchFilter()));
        allfilters.add(Arrays.asList("RBG Switch", new RBGSwitchFilter()));
        allfilters.add(Arrays.asList("RBR Switch", new RBRSwitchFilter()));

        allfilters.add(Arrays.asList("Solarize", new GPUImageSolarizeFilter()));
        allfilters.add(Arrays.asList("Solarize 2", blendEffectFilters.getSolarizeVariantFilter()));
        allfilters.add(Arrays.asList("Solarus", blendEffectFilters.getSolarusFilter()));
        allfilters.add(Arrays.asList("False Color", new GPUImageFalseColorFilter()));
        allfilters.add(Arrays.asList("Duotone", blendEffectFilters.getDuotoneFilter()));
        allfilters.add(Arrays.asList("Duotone2", blendEffectFilters.getDuotone2Filter()));
        allfilters.add(Arrays.asList("Pop Art", blendEffectFilters.getPopartFilter()));

        return allfilters;
    }


    public List<List<Object>> createOtherEffectsFilters() {

        List<List<Object>> allfilters = new ArrayList<>();

        allfilters.add(Arrays.asList("Posterize", new GPUImagePosterizeFilter()));
        allfilters.add(Arrays.asList("Half Tone", new GPUImageHalftoneFilter()));
        allfilters.add(Arrays.asList("RGB Dilation", new GPUImageRGBDilationFilter()));
        allfilters.add(Arrays.asList("Vibrance", new GPUImageVibranceFilter()));
        allfilters.add(Arrays.asList("Toon", new GPUImageToonFilter()));
        allfilters.add(Arrays.asList("Smooth Toon", new GPUImageSmoothToonFilter()));

        return allfilters;
    }



}
