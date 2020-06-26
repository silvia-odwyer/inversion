package com.silviaodwyer.inversion;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Video {
  private VideoMetadata metadata;
  private Bitmap thumbnail;

  public Video(Bitmap thumbnail) {
    this.metadata = new VideoMetadata();
    this.thumbnail = thumbnail;
  }

  public Video(VideoMetadata videoMetadata) {
    this.metadata = videoMetadata;
    this.thumbnail = getThumbnailFromPath();
  }

  public VideoMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(VideoMetadata metadata) {
    this.metadata = metadata;
  }

  private Bitmap getThumbnailFromPath() {
    String bitmapPath = metadata.getThumbnailPath();
    Bitmap thumbnail = BitmapFactory.decodeFile(bitmapPath);
    Log.d("DEBUG", "THUMBNAIL PATH IS: " + bitmapPath);
    return thumbnail;
  }

  public Bitmap getThumbnail() {
    return this.thumbnail;
  }

}
