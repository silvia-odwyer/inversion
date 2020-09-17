package com.silviaodwyer.inversion.video_filters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Size;
import com.daasuu.gpuv.egl.filter.GlFilter;

/**
 * Utility blend class adapted from GPUVideo library filters at
 * https://github.com/MasayukiSuda/GPUVideo-android
 * which sets up textures and draws the bitmap accordingly
 * in line with the library.
 */
public abstract class GlBlendFilter extends GlFilter {
    private Bitmap bitmap = null;
    private int[] textures = new int[1];
    private static int height = 1280;
    private static int width = 720;
    private Size inputSize = new Size(width, height);

    GlBlendFilter(String FRAGMENT_SHADER) {
        super(DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
    }

    private void setResolution(Size resolution) {
        this.inputSize = resolution;
    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        setResolution(new Size(width, height));
    }

    private void createBitmap() {
        // release bitmap if still in use to enhance performance and prevent memory leaks.
        releaseBitmap(bitmap);

        // create a new bitmap with width and height of input resolution
        bitmap = Bitmap.createBitmap(inputSize.getWidth(), inputSize.getHeight(), Bitmap.Config.ARGB_8888);
    }

    @Override
    public void setup() {
        super.setup();
        initTextures();
        createBitmap();
    }

    @Override
    public void onDraw() {
        // recreate bitmap if none exists
        if (bitmap == null) {
            createBitmap();
        }
        if (bitmap.getWidth() != inputSize.getWidth() || bitmap.getHeight() != inputSize.getHeight()) {
            // create the bitmap again if bitmap width not equal to input size width or input size height
            createBitmap();
        }

        eraseBitmap();
        Canvas bmpCanvas = new Canvas(bitmap);
        bmpCanvas.scale(1, -1, bmpCanvas.getWidth() / 2, bmpCanvas.getHeight() / 2);
        drawCanvas(bmpCanvas);

        int offsetDepthMapTextureUniform = getHandle("oTexture");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        // bind texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        if (bitmapExists()) {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);
        }

        GLES20.glUniform1i(offsetDepthMapTextureUniform, 3);
    }


    private void initTextures() {
        // initialize textures to be used
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }

    protected void drawCanvas(Canvas canvas) {
        if (bitmapExists()) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }
    private static void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    private Boolean bitmapExists() {
        if (bitmap != null && !bitmap.isRecycled()) {
            return true;
        }
        return false;
    }

    private void eraseBitmap() {
        bitmap.eraseColor(Color.argb(0, 0, 0, 0));
    }
}
