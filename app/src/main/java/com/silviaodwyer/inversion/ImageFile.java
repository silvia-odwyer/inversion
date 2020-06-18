package com.silviaodwyer.inversion;

import android.graphics.Bitmap;

public class ImageFile {
  private Bitmap bitmap;
  private FileMetadata metadata;

  public ImageFile(Bitmap bitmap, FileMetadata metadata) {
    this.metadata = metadata;
    this.bitmap = bitmap;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  public FileMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(FileMetadata metadata) {
    this.metadata = metadata;
  }
}
