package com.silviaodwyer.inversion;

import android.graphics.Bitmap;

public class ImageFile {
  private Bitmap bitmap;
  private ImageMetadata metadata;

  public ImageFile(Bitmap bitmap, ImageMetadata metadata) {
    this.metadata = metadata;
    this.bitmap = bitmap;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  public ImageMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(ImageMetadata metadata) {
    this.metadata = metadata;
  }
}
