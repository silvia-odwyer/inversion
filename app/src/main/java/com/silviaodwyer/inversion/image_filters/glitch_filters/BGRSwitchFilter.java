package com.silviaodwyer.inversion.image_filters.glitch_filters;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class BGRSwitchFilter extends GPUImageFilter {
    public static final String SWITCH_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "    \n" +
            "   gl_FragColor = vec4(textureColor.b, textureColor.g, textureColor.r, 1.0);\n" +
            "}";

    public BGRSwitchFilter() {
        super(NO_FILTER_VERTEX_SHADER, SWITCH_FRAGMENT_SHADER);
    }
}