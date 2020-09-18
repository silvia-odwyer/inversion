package com.silviaodwyer.inversion.image_filters.glitch_filters;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class ChromaticAbberationFilter extends GPUImageFilter {
    public static final String BLUE_SHIFT_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    lowp vec2 ot = vec2(.01, .0);\n" +

            "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "    textureColor.b = texture2D(inputImageTexture, textureCoordinate + ot.yx).b;\n" +
            "    textureColor.r = texture2D(inputImageTexture, textureCoordinate + ot.xy).r;\n" +
            "    \n" +
            "   gl_FragColor = vec4(textureColor.r, textureColor.g, textureColor.b, 1.0);\n" +
            "}";

    public ChromaticAbberationFilter() {
        super(NO_FILTER_VERTEX_SHADER, BLUE_SHIFT_FRAGMENT_SHADER);
    }
}