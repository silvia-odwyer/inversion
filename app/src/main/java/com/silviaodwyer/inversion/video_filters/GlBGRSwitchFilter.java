package com.silviaodwyer.inversion.video_filters;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.daasuu.gpuv.egl.filter.GlFilter;

public class GlBGRSwitchFilter extends GlFilter {

    private static final String FRAGMENT_SHADER =
            "precision mediump float;" +
                    "varying vec2 vTextureCoord;" +
                    "uniform lowp sampler2D sTexture;" +
                    "void main() {" +
                    "   vec4 fragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "   gl_FragColor = vec4(fragColor.b, fragColor.g, fragColor.r, 1.0);\n" +
                    "}";

    public GlBGRSwitchFilter() {
        super(DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
    }

}
