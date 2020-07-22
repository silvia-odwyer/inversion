package com.silviaodwyer.inversion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

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
        resultBitmap = resizeBitmap(resultBitmap, maxWidth, maxHeight);
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
  }

  /**
   * Convert an image URI to a bitmap.
   */
  public Bitmap imageUriToBitmap(Uri imageUri) {
    Bitmap bmp = null;
    try {
      bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bmp;
  }

  /**
   * Resize a bitmap.
   */
  public Bitmap resizeBitmap(Bitmap bitmap, float maxWidth, float maxHeight) {
    float scale = Math.min(((float) maxHeight / bitmap.getWidth()), ((float) maxWidth / bitmap.getHeight()));
    // resize bitmap to thumbnail size
    Matrix matrix = new Matrix();
    matrix.postScale(scale, scale);
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
  }

  /**
   * Get the locale of the user's device. This is required
   * for date formatting according to the locale when generating
   * image names, etc.,
   *
   * @return locale
   */
  private Locale getLocale() {
    // check if API Level is greater than 24
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return context.getResources().getConfiguration().getLocales().get(0);
    } else {
      Log.d("DEBUG", "API Level less than 24");
      return context.getResources().getConfiguration().locale;
    }
  }

  /**
   * Given an image's name, retrieves the image as a bitmap from the device's external storage.
   */
  public static Image getImageFromFilename(ImageMetadata metadata, Context context, MainApplication mainApplication) {
    Image image = null;
    if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
      File imageFile = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/images", metadata.getName() + ".png");

      Bitmap bitmap = null;
      try {
        bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      if (bitmap == null) {
        Log.d("DEBUG", "BITMAP IS NULL");
      } else {
        image = new Image(bitmap, context, mainApplication.getImageEditorActivity(), metadata);

      }
    }
    return image;
  }

  /**
   * Save a video's thumbnail to the device's external storage.
   */
  public void writeThumbnail(Video video) {
    FileOutputStream fileOutputStream = null;
    try {
      File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos/thumbnails");
      directory.mkdirs();

      File outputFile = new File(directory.toString(), video.getMetadata().getName() + ".png");
      Log.d("DEBUG", "THUMBNAIL OUTPUTTED TO: " + outputFile.getAbsolutePath());

      fileOutputStream = new FileOutputStream(outputFile);
      video.getThumbnail().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
    } catch (IOException e) {
      Log.d("DEBUG", "Could not write file!" + e);
    } finally {
      try {
        fileOutputStream.close();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public Bitmap getBitmapFromUrl(String url_string) {
    Bitmap bitmap = null;
    try {
      URL url = new URL(url_string);
      bitmap = BitmapFactory.decodeStream(url.openStream());
    } catch (IOException e) {
      System.out.println(e);
    }
    return bitmap;
  }


  public static ArrayList<ImageMetadata> createImageMetadataFromURLs(String[] urls) {
    ArrayList<ImageMetadata> fileMetadataArrayList = new ArrayList<>();
    for (String url : urls) {
      ImageMetadata metadata = new ImageMetadata(url);
      fileMetadataArrayList.add(metadata);
    }
    return fileMetadataArrayList;
  }
}