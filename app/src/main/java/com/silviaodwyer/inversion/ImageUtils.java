package com.silviaodwyer.inversion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.silviaodwyer.inversion.ImageEditor;
import com.silviaodwyer.inversion.R;

import java.io.IOException;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;

public class ImageUtils {
  private Bitmap originalImageBitmap;
  private ArrayList<GPUImageFilter> filters;
  private ArrayList<Bitmap> filteredImages;
  private Context context;

  private Uri imageUri;

  public ImageUtils(Context ctx) {
    this.context = ctx;
  }

  public void appendFilteredImageThumbnails(LinearLayout filteredImagesLinLayout) {

    GPUImage mGPUImage = new GPUImage(context);
    Bitmap bmp = null;
    try {
      bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
      originalImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (bmp == null) {
      Log.d("DEBUG", "bitmap is null");
    }
    mGPUImage.setImage(bmp);

    bmp = mGPUImage.getBitmapWithFilterApplied(bmp);
    filters = new ArrayList<GPUImageFilter>();

    filters.add(new GPUImageSepiaToneFilter());
    filters.add(new GPUImageToonFilter());
    GPUImage.getBitmapForMultipleFilters(bmp, filters, new GPUImage.ResponseListener<Bitmap>() {

      @Override
      public void response(Bitmap resultBitmap) {
        if (resultBitmap == null) {
          Log.d("DEBUG", "Bitmap res null");
        }
        int maxHeight = 150;
        int maxWidth = 150;
        float scale = Math.min(((float)maxHeight / resultBitmap.getWidth()), ((float)maxWidth / resultBitmap.getHeight()));
        // resize bitmap to thumbnail size
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        resultBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);

        filteredImages.add(resultBitmap);
      }
    });

    for (int filter_index = 0; filter_index < filteredImages.size(); filter_index++) {
      final int filter_index_final = filter_index;
      final ImageView imageView = new ImageView(context);
      imageView.setImageBitmap(filteredImages.get(filter_index));
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//          filterImage(filters.get(filter_index_final), mGPUImage);
        }
      });

      filteredImagesLinLayout.addView(imageView);
    }
  }

  public void filterImage(GPUImageFilter filter, GPUImage gpuImage) {
    gpuImage.setFilter(filter);
    Bitmap bmp = gpuImage.getBitmapWithFilterApplied(originalImageBitmap);
    //activity.updateGPUImage();
  }

  public Bitmap imageUriToBitmap(Uri imageUri) {
    Bitmap bmp = null;
    try {
      bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bmp;
  }

  public Bitmap resizeBitmap(Bitmap bitmap, float maxWidth, float maxHeight) {
    float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));
    // resize bitmap to thumbnail size
    Matrix matrix = new Matrix();
    matrix.postScale(scale, scale);

    final Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    return resultBitmap;
  }

}
