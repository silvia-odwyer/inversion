package com.silviaodwyer.inversion.video_filters;

import com.daasuu.gpuv.egl.filter.GlFilter;

public class GlBlueGlitchLines extends GlFilter {

    private static final String FRAGMENT_SHADER =
            "precision mediump float;" +
                    "varying vec2 vTextureCoord;" +
                    "uniform lowp sampler2D sTexture;" +
                    "void main() {" +
                    "   vec2 ot = vec2(.4,.0);\n" +
                    "   vec4 fragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "   fragColor.b = texture2D(sTexture, vTextureCoord + ot.xy).b;\n" +
                    "   gl_FragColor = vec4(fragColor.r, fragColor.g, fragColor.b, 1.0);\n" +
                    "}";

    public GlBlueGlitchLines() {
        super(DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
    }

}
