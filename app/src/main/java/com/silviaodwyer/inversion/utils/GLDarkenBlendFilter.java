package com.silviaodwyer.inversion.utils;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GLDarkenBlendFilter extends GlBlendFilter {
    private Bitmap bitmap;
    private static String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "uniform lowp sampler2D oTexture;\n" +
                    "void main() {\n" +
                    "   lowp vec4 overlayTexture = texture2D(sTexture, vTextureCoord);\n" +
                    "   lowp vec4 baseTexture = texture2D(oTexture, vTextureCoord);\n" +
                    "   gl_FragColor = vec4(min(overlayTexture.rgb * baseTexture.a, baseTexture.rgb * overlayTexture.a) + overlayTexture.rgb * (1.0 - baseTexture.a) + baseTexture.rgb * (1.0 - overlayTexture.a), 1.0);\n" +
                    "}\n";

    public GLDarkenBlendFilter(Bitmap bitmap) {
        super(FRAGMENT_SHADER);
        this.bitmap = bitmap;
    }

    @Override
    protected void drawCanvas(Canvas canvas) {
        if (bitmap != null && !bitmap.isRecycled()) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

}
