package com.silviaodwyer.inversion.video_filters;

import com.daasuu.gpuv.egl.filter.GlFilter;

public class GlBRGSwitchFilter extends GlFilter {

    private static final String FRAGMENT_SHADER =
            "precision mediump float;" +
                    "varying vec2 vTextureCoord;" +
                    "uniform lowp sampler2D sTexture;" +
                    "void main() {" +
                    "   vec4 fragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "   vec4 green = texture2D(sTexture, vTextureCoord);\n" +
                    "   gl_FragColor = vec4(fragColor.b, fragColor.r, fragColor.g, 1.0);\n" +
                    "}";

    public GlBRGSwitchFilter() {
        super(DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
    }

}
