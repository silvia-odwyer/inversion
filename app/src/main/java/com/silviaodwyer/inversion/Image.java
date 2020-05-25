package com.silviaodwyer.inversion;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;

public class Image {
  private GPUImage mGPUImage;
  private Bitmap originalImageBitmap;
  private Uri imageUri;
  private Context context;
  private ImageEditor activity;
  private ArrayList<Bitmap> filteredThumbnails;

  public void addFilteredThumbnail(Bitmap thumbnail) {
    this.filteredThumbnails.add(thumbnail);
  }

  public Image(Uri imageUri, Context ctx, ImageEditor activity) {
    this.context = ctx;
    this.imageUri = imageUri;
    this.originalImageBitmap = generateOriginalBitmap();
    filteredThumbnails = new ArrayList<>();
    this.activity = activity;
    this.createGPUImage();
  }

  public void createGPUImage() {
    mGPUImage = new GPUImage(context);
    mGPUImage.setImage(originalImageBitmap);
  }

  public ImageEditor getActivity() {
    return activity;
  }

  public void setActivity(ImageEditor activity) {
    this.activity = activity;
  }

  public ArrayList<Bitmap> getFilteredThumbnails() {
    return filteredThumbnails;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public void setFilteredThumbnails(ArrayList<Bitmap> filteredThumbnails) {
    this.filteredThumbnails = filteredThumbnails;
  }

  private Bitmap generateOriginalBitmap() {
    Bitmap bmp = null;
    try {
      bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bmp;
  }


  public Uri getImageUri() {
    return imageUri;
  }

  public void setImageUri(Uri imageUri) {
    this.imageUri = imageUri;
  }

  public Bitmap getOriginalImageBitmap() {
    return originalImageBitmap;
  }

  public void setOriginalImageBitmap(Bitmap originalImageBitmap) {
    this.originalImageBitmap = originalImageBitmap;
  }

  public GPUImage getmGPUImage() {
    return mGPUImage;
  }

  public void setmGPUImage(GPUImage mGPUImage) {
    this.mGPUImage = mGPUImage;
  }
}
