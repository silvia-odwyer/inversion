package com.silviaodwyer.inversion.image_filters.glitch_filters;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class GreenShiftFilter extends GPUImageFilter {
    public static final String GREEN_SHIFT_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "   lowp vec4 green = texture2D(inputImageTexture, textureCoordinate - 0.021);\n" +
            "    \n" +
            "   gl_FragColor = vec4(textureColor.r, green.g, textureColor.b, 1.0);\n" +
            "}";

    public GreenShiftFilter() {
        super(NO_FILTER_VERTEX_SHADER, GREEN_SHIFT_FRAGMENT_SHADER);
    }
}