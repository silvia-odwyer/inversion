package com.silviaodwyer.inversion.image_filters.glitch_filters;

import android.opengl.GLES20;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class TintFilter extends GPUImageFilter {
    private float red = 0.3f;
    private float green = 0.8f;
    private float blue = 1.9f;
    private int redLoc;
    private int greenLoc;
    private int blueLoc;

    public static final String RED_SHIFT_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            " uniform lowp float red;\n" +
            " uniform lowp float green;\n" +
            " uniform lowp float blue;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "    \n" +
            "   gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);\n" +
            "}";

    public TintFilter(float red, float green, float blue) {
        super(NO_FILTER_VERTEX_SHADER, RED_SHIFT_FRAGMENT_SHADER);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public void onInit() {
        super.onInit();
        redLoc = GLES20.glGetUniformLocation(getProgram(), "red");
        greenLoc = GLES20.glGetUniformLocation(getProgram(), "green");
        blueLoc = GLES20.glGetUniformLocation(getProgram(), "blue");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

    public void setRed(final float red) {
        this.red = red;
        setFloat(redLoc, this.red);
    }

    public void setGreen(final float green) {
        this.green = green;
        setFloat(greenLoc, this.green);
    }

    public void setBlue(final float blue) {
        this.blue = blue;
        setFloat(blueLoc, this.blue);
    }


}