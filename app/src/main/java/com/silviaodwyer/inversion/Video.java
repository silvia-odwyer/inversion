package com.silviaodwyer.inversion;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

public class Video {
  private VideoMetadata metadata;

  public Video() {
    this.metadata = new VideoMetadata();
  }

  public Video(VideoMetadata videoMetadata) {
    this.metadata = videoMetadata;
  }

  public VideoMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(VideoMetadata metadata) {
    this.metadata = metadata;
  }

  public Bitmap getThumbnail() {
    String bitmapPath = metadata.getThumbnailPath();
    Bitmap thumbnail = BitmapFactory.decodeFile(bitmapPath);
    return thumbnail;
  }


}
