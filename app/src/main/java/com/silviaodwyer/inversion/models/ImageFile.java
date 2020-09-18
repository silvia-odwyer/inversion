package com.silviaodwyer.inversion.models;

import android.graphics.Bitmap;

import com.silviaodwyer.inversion.models.FileMetadata;

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
