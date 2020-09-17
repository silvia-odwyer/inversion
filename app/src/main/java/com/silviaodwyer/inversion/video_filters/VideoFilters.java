package com.silviaodwyer.inversion.video_filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.widget.Toast;

import com.daasuu.gpuv.egl.filter.*;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.silviaodwyer.inversion.image_filters.GradientFilters;
import com.silviaodwyer.inversion.image_filters.ImageFilterPacks;
import com.silviaodwyer.inversion.image_filters.ImageFilters;
import com.silviaodwyer.inversion.utils.ImageUtils;
import com.silviaodwyer.inversion.VideoThumbnail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;

public class VideoFilters {
  private ArrayList<GlFilter> videoFilters = new ArrayList<>();
  private Context context;
  private ImageFilterPacks imageFilterPacks;
  private List<Integer> bitmaps;

  public VideoFilters(Context context) {
    this.context = context;
    imageFilterPacks = new ImageFilterPacks(context);
    bitmaps = GradientFilters.getBlendBackgrounds();

  }

    public List<List<Object>> createBlendFilters() {
        List<List<Object>> blendFilters = new ArrayList<>();
        for (int k = 0; k < bitmaps.size(); k++) {
            Integer bitmapResource = bitmaps.get(k);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResource);
            blendFilters.add(Arrays.asList("Gradient " + k, new GLSoftLightBlendFilter(bitmap)));

        }
        return blendFilters;
    }

    public List<List<Object>> createColorBlendFilters() {
        List<List<Object>> blendFilters = new ArrayList<>();
        for (int k = 0; k < bitmaps.size(); k++) {
            Integer bitmapResource = bitmaps.get(k);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResource);
            blendFilters.add(Arrays.asList("Color Blend " + k, new GLMultiplyBlendFilter(bitmap)));

        }
        return blendFilters;
    }

  public ArrayList<GlFilter> getVideoFilters() {
    return videoFilters;
  }

  public GlFilterGroup vignetteGrayscaleFilter() {
    return new GlFilterGroup(new GlGrayScaleFilter(), new GlVignetteFilter());
  }

  public GlFilter hueRotateFilter(float amt) {
    GlHueFilter glHueFilter = new GlHueFilter();
    glHueFilter.setHue(amt);
    return glHueFilter;
  }

  public GlFilter vibrantFilter() {
      GlSaturationFilter saturationFilter = new GlSaturationFilter();
      saturationFilter.setSaturation(1.3f);
      return saturationFilter;
  }

    public GlFilterGroup obsidianFilter() {
        GlContrastFilter contrastFilter = new GlContrastFilter();
        contrastFilter.setContrast(1.4f);
        GlFilterGroup filterGroup = new GlFilterGroup(new GlGrayScaleFilter(), contrastFilter);

        return filterGroup;
    }

    public GlFilter dramaticFilter() {
        GlContrastFilter contrastFilter = new GlContrastFilter();
        contrastFilter.setContrast(1.1f);

        return new GlFilterGroup(new GlGrayScaleFilter(), contrastFilter);
    }

    public GlFilter monochromeFilter() {
        GlMonochromeFilter monochromeFilter = new GlMonochromeFilter();
        monochromeFilter.setIntensity(1.2f);
        return monochromeFilter;
    }

    public GlFilter posterizeFilter() {
        GlPosterizeFilter posterizeFilter = new GlPosterizeFilter();
        posterizeFilter.setColorLevels(12);
        return posterizeFilter;
    }


    public GlFilterGroup sepiaVignetteFilter() {
    return new GlFilterGroup(new GlSepiaFilter(), new GlVignetteFilter());
  }

  public GlFilterGroup vividFilter() {
      GlSaturationFilter saturationFilter = new GlSaturationFilter();
      saturationFilter.setSaturation(1.5f);
      GlContrastFilter contrastFilter = new GlContrastFilter();
      contrastFilter.setContrast(1.5f);
      GlHueFilter hueFilter = new GlHueFilter();
      hueFilter.setHue(0.2f);
      GlFilterGroup group = new GlFilterGroup(saturationFilter, contrastFilter, hueFilter);
      return group;
  }

  public GlFilterGroup lomoFilter() {
      return new GlFilterGroup(vividFilter(), new GlVignetteFilter());
  }

    public GlFilterGroup lomoFuschiaFilter() {
        GlSaturationFilter saturationFilter = new GlSaturationFilter();
        saturationFilter.setSaturation(1.5f);
        GlContrastFilter contrastFilter = new GlContrastFilter();
        contrastFilter.setContrast(1.5f);
        GlHueFilter hueFilter = new GlHueFilter();
        hueFilter.setHue(1.6f);
        return new GlFilterGroup(saturationFilter, contrastFilter, hueFilter,  new GlVignetteFilter());
    }

  public GlFilterGroup rubrikFilter() {
    GlBrightnessFilter brightnessFilter = new GlBrightnessFilter();
    brightnessFilter.setBrightness((float) 0.5);
    GlContrastFilter contrastFilter = new GlContrastFilter();
    contrastFilter.setContrast((float) 1.2);

    GlFilterGroup rubrikFilter = new GlFilterGroup(new GlContrastFilter(), brightnessFilter );
    return rubrikFilter;
  }

  public GlFilterGroup neueFilter() {
    GlFilter bilateralFilter = new GlBilateralFilter();
    GlFilterGroup filterGroup = new GlFilterGroup(bilateralFilter);
    return filterGroup;
  }

  /**
   * Filter the video by applying the filter passed.
   **/
  public void filterVideo(String filterName, GPUPlayerView ePlayerView) {
      GlFilter filter = getVideoFilterFromName(filterName);
    ePlayerView.setGlFilter(filter);

  }

  public GlFilter getVideoFilterFromName(String filterName) {
      String category = ImageFilters.filterInCategory(filterName);

      if (category != null) {
          // image filter is a category filter
      switch(category) {
          case "Gradient":
              Integer bitmapResource = bitmaps.get(0);
              Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResource);
              return new GLSoftLightBlendFilter(bitmap);
      }
      }
      switch(filterName) {
          case "Sepia":
              return new GlSepiaFilter();
          case "Grayscale":
              return new GlGrayScaleFilter();
          case "Grayscale Vignette":
              return vignetteGrayscaleFilter();
          case "Sepia Vignette":
              return sepiaVignetteFilter();
          case "Vivid":
              return vividFilter();
          case "Vignette":
              return new GlVignetteFilter();
          case "Lomo":
              return lomoFilter();
          case "Lomo Fuschia":
              return lomoFuschiaFilter();
          case "Rubrik":
              return rubrikFilter();
          case "Hue Rotate 2":
              return hueRotateFilter(0.8f);
          case "Hue Rotate 3":
              return hueRotateFilter(0.2f);
          case "Neue":
              return neueFilter();
          case "Monochrome":
              return monochromeFilter();
          case "Vibrant":
              return vibrantFilter();
          case "Dramatic":
              return dramaticFilter();
          case "Obsidian":
              return obsidianFilter();
          case "Hue Rotate":
              return hueRotateFilter(1.2f);
          case "Hue Rotate 4":
              return hueRotateFilter(1.8f);
          case "Posterize":
              return posterizeFilter();
          case "Chromatic Abberation":
              return new ChromaticAbberationFilter();
          case "Chromatic Abberation 2":
              return new GlChromaticAbberationTwoFilter();
          case "Invert":
              return new GlInvertFilter();
          case "Red Glitch":
              return new GlGlitchRedLines();
          case "Green Glitch":
              return new GlRedShiftFilter();
          case "Blue Glitch":
              return new GlBlueGlitchLines();
          case "Red Shift":
              return new GlRedShiftFilter();
          case "Green Shift":
              return new GlGreenShiftFilter();
          case "Blue Shift":
          return new GlBlueShiftFilter();
          case "Switcher":
              return new GlSwitchFilter();
          case "Switcher 2":
              return new GlBGRSwitchFilter();
          case "Switcher 3":
              return new GlRBGSwitchFilter();
          case "Switcher 4":
              return new GlGRBSwitchFilter();
          case "Switcher 5":
              return new GlBRGSwitchFilter();
          case "GBR Switch":
              return new GlGBRSwitchFilter();
          default:
              Toast.makeText(context, "Video filter not found", Toast.LENGTH_SHORT).show();
              return new GlSepiaFilter();
      }
  }

    public static class TintFilter {
        private Bitmap bitmap;
        private static String FRAGMENT_SHADER =
                "precision mediump float;\n" +
                        "varying vec2 vTextureCoord;\n" +
                        "uniform lowp sampler2D sTexture;\n" +
                        "uniform lowp sampler2D oTexture;\n" +
                        "void main() {\n" +
                        "   lowp vec4 overlayTexture = texture2D(sTexture, vTextureCoord);\n" +
                        "   lowp vec4 baseTexture = texture2D(oTexture, vTextureCoord);\n" +
                        "   gl_FragColor = vec4(min(overlayTexture.rgb * baseTexture.a, baseTexture.rgb * overlayTexture.a) + overlayTexture.rgb * (1.0 - baseTexture.a) + baseTexture.rgb * (1.0 - overlayTexture.a), 1.0);\n" +
                        "}\n";

    //    public GLBlendFilter(Bitmap bitmap) {
    //        super(FRAGMENT_SHADER);
    //        this.bitmap = bitmap;
    //    }
    //
    //    @Override
    //    protected void drawCanvas(Canvas canvas) {
    //        if (bitmap != null && !bitmap.isRecycled()) {
    //            canvas.drawBitmap(bitmap, 0, 0, null);
    //        }
    //    }

    }

    public static class GLSoftLightBlendFilter extends GlBlendFilter {

        private Bitmap bitmap;
        private static String FRAGMENT_SHADER =
                "precision mediump float;\n" +
                        "varying vec2 vTextureCoord;\n" +
                        "uniform lowp sampler2D sTexture;\n" +
                        "uniform lowp sampler2D oTexture;\n" +
                        "void main() {\n" +
                        "   lowp vec4 overlayTexture = texture2D(sTexture, vTextureCoord);\n" +
                        "   lowp vec4 baseTexture = texture2D(oTexture, vTextureCoord);\n" +
                        "   gl_FragColor = baseTexture * (overlayTexture.a * (baseTexture / baseTexture.a) + (2.0 * overlayTexture * (1.0 - (baseTexture / baseTexture.a)))) + overlayTexture * (1.0 - baseTexture.a) + baseTexture * (1.0 - overlayTexture.a);\n" +
                        "}\n";

        public GLSoftLightBlendFilter(Bitmap bitmap) {
            super(FRAGMENT_SHADER);
            this.bitmap = bitmap;
        }

        @Override
        protected void drawCanvas(Canvas canvas) {
            if (bitmap != null && !bitmap.isRecycled()) {
                canvas.drawBitmap(bitmap, 0, 0, null);
            }
        }

    }
}
