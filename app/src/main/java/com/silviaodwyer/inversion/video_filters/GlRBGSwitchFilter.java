package com.silviaodwyer.inversion.video_filters;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.daasuu.gpuv.egl.filter.GlFilter;

public class GlRBGSwitchFilter extends GlFilter {

    private static final String FRAGMENT_SHADER =
            "precision mediump float;" +
                    "varying vec2 vTextureCoord;" +
                    "uniform lowp sampler2D sTexture;" +
                    "void main() {" +
                    "   vec4 fragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "   vec4 green = texture2D(sTexture, vTextureCoord);\n" +
                    "   gl_FragColor = vec4(fragColor.r, fragColor.b, fragColor.g, 1.0);\n" +
                    "}";

    public GlRBGSwitchFilter() {
        super(DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
    }

}
