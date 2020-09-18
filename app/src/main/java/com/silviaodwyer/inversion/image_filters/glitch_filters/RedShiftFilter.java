package com.silviaodwyer.inversion.image_filters.glitch_filters;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class RedShiftFilter extends GPUImageFilter {
    public static final String RED_SHIFT_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "   lowp vec4 red = texture2D(inputImageTexture, textureCoordinate - 0.021);\n" +
            "    \n" +
            "   gl_FragColor = vec4(red.r, textureColor.g, textureColor.b, 1.0);\n" +
            "}";

    public RedShiftFilter() {
        super(NO_FILTER_VERTEX_SHADER, RED_SHIFT_FRAGMENT_SHADER);
    }
}