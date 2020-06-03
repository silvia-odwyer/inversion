package com.silviaodwyer.inversion;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;

public class Image {
  private GPUImage mGPUImage;
  private Bitmap bitmap;
  private Bitmap originalImageBitmap;
  private Context context;
  private ImageEditor activity;
  private ArrayList<Bitmap> filteredThumbnails;
  private String path;

  public ArrayList<Bitmap> getCorrectedThumbnails() {
    return correctedThumbnails;
  }

  public void setCorrectedThumbnails(ArrayList<Bitmap> correctedThumbnails) {
    this.correctedThumbnails = correctedThumbnails;
  }

  private ArrayList<Bitmap> correctedThumbnails;

  public void addFilteredThumbnail(Bitmap thumbnail) {
    this.filteredThumbnails.add(thumbnail);
  }

  public Image(Bitmap bitmap, Context ctx, ImageEditor activity) {
    this.context = ctx;
    this.bitmap = bitmap;
    this.originalImageBitmap = bitmap;
    filteredThumbnails = new ArrayList<>();
    correctedThumbnails = new ArrayList<>();
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

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
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

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
  }
}
