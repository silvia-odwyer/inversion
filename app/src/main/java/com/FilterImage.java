package com;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
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

public class FilterImage {
  private Bitmap originalImageBitmap;
  private ArrayList<GPUImageFilter> filters;
  private ArrayList<Bitmap> filteredImages;
  private Context context;
  private Activity activity;
  private Uri imageUri;

  public FilterImage(Context ctx, Activity activity, Uri uri) {
    this.context = ctx;
    this.activity = activity;
    this.imageUri = uri;
  }

  public void appendFilteredImageThumbnails(LinearLayout filteredImagesLinLayout) {

    final GPUImage mGPUImage = new GPUImage(context);
    Bitmap bmp = null;
    try {
      bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
      originalImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    } catch (IOException e) {
      e.printStackTrace();
    }
    mGPUImage.setImage(bmp);

    bmp = mGPUImage.getBitmapWithFilterApplied(bmp);
    filters = new ArrayList<GPUImageFilter>();

    filters.add(new GPUImageSepiaToneFilter());
    filters.add(new GPUImageToonFilter());
    mGPUImage.getBitmapForMultipleFilters(bmp,filters, new GPUImage.ResponseListener<Bitmap>() {

      @Override
      public void response(Bitmap resultBitmap) {
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
          filterImage(filters.get(filter_index_final), mGPUImage);
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
}
