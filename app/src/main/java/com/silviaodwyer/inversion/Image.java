package com.silviaodwyer.inversion;

import android.content.Context;
import android.graphics.Bitmap;
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
  private ArrayList<Bitmap> correctedThumbnails;
  private ImageMetadata metaData;

  /**
   * Returns all image thumbnails with image correction filters applied to them.
   *
   * @return      image thumbnails with image correction filters applied to them
   */
  public ArrayList<Bitmap> getCorrectedThumbnails() {
    return correctedThumbnails;
  }

  public void setCorrectedThumbnails(ArrayList<Bitmap> correctedThumbnails) {
    this.correctedThumbnails = correctedThumbnails;
  }

  public void addFilteredThumbnail(Bitmap thumbnail) {
    this.filteredThumbnails.add(thumbnail);
  }

  public Image(Bitmap bitmap, Context ctx, ImageEditor activity, ImageMetadata metaData) {
    this.context = ctx;
    this.bitmap = bitmap;
    this.originalImageBitmap = bitmap;
    filteredThumbnails = new ArrayList<>();
    correctedThumbnails = new ArrayList<>();
    this.activity = activity;
    this.metaData = metaData;
    this.createGPUImage();
  }

  public void createGPUImage() {
    mGPUImage = new GPUImage(context);
    mGPUImage.setImage(originalImageBitmap);
  }

  /**
   * Returns the image editor activity for this application.
   *
   * @return      the image editor activity
   */
  public ImageEditor getActivity() {
    return activity;
  }

  /**
   * Set the image editor activity for this application.
   *
   */
  public void setActivity(ImageEditor activity) {
    this.activity = activity;
  }

  public ArrayList<Bitmap> getFilteredThumbnails() {
    return filteredThumbnails;
  }

  /**
   * Get the context for this application.
   *
   * @return    context for this application
   */
  public Context getContext() {
    return context;
  }

  /**
   * Set the context for this application.
   *
   */
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

  /**
   * Get the original image bitmap, without filters
   * applied.
   *
   * @return    original bitmap without filters applied
   */
  public Bitmap getOriginalImageBitmap() {
    return originalImageBitmap;
  }

  /**
   * Set the original image bitmap for this application.
   *
   */
  public void setOriginalImageBitmap(Bitmap originalImageBitmap) {
    this.originalImageBitmap = originalImageBitmap;
  }

  public Bitmap getThumbnail(int maxWidth, int maxHeight) {
    ImageUtils imageUtils = new ImageUtils(context);
    final Bitmap resultBitmap = imageUtils.resizeBitmap(bitmap, maxWidth, maxHeight);
    return resultBitmap;
  }

  /**
   * Returns the GPUImage instance of the image.
   *
   * @return  the GPUImage instance of the image
   */
  public GPUImage getmGPUImage() {
    return mGPUImage;
  }

  /**
   * Set the GPUImage instance of the image, which
   * allows image processing to be performed on the GPU.
   *
   * @return      the image editor activity
   */
  public void setmGPUImage(GPUImage mGPUImage) {
    this.mGPUImage = mGPUImage;
    this.bitmap = mGPUImage.getBitmapWithFilterApplied();
  }

  /**
   * Returns the bitmap for this image.
   *
   * @return      the image's bitmap
   */
  public Bitmap getBitmap() {
    return bitmap;
  }

  /**
   * Set the bitmap for this image.
   *
   */
  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
    mGPUImage.setImage(bitmap);
  }

  /**
   * Get the metadata for this image.
   *
   * @return metadata for this image
   */
  public ImageMetadata getMetaData() {
    return metaData;
  }

  /**
   * Set the metadata for this image.
   *
   */
  public void setMetaData(ImageMetadata metaData) {
    this.metaData = metaData;
  }
}
