package com.silviaodwyer.inversion.video_filters;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.daasuu.gpuv.egl.filter.GlFilter;

public class GlBlueShiftFilter extends GlFilter {

    private static final String FRAGMENT_SHADER =
            "precision mediump float;" +
                    "varying vec2 vTextureCoord;" +
                    "uniform lowp sampler2D sTexture;" +
                    "void main() {" +
                    "   vec4 fragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "   vec4 blue = texture2D(sTexture, vTextureCoord - 0.021);\n" +
                    "   gl_FragColor = vec4(fragColor.r, fragColor.g, blue.b, 1.0);\n" +
                    "}";

    public GlBlueShiftFilter() {
        super(DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
    }

}
