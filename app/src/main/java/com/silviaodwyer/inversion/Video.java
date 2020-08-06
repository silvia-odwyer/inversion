package com.silviaodwyer.inversion;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Video {
  private VideoMetadata metadata;
  private Bitmap thumbnail;

  public Video(Bitmap thumbnail, String videoUrl) {
    this.metadata = new VideoMetadata(videoUrl);
    this.thumbnail = thumbnail;
  }

  public Video(VideoMetadata videoMetadata) {
    this.metadata = videoMetadata;
    this.thumbnail = getThumbnailFromPath();
  }

  /**
   * Get the video metadata for this video.
   *
   * @return    video metadata
   */
  public VideoMetadata getMetadata() {
    return metadata;
  }

  /**
   * Set the video metadata for this video.
   *
   */
  public void setMetadata(VideoMetadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Fetch the thumbnail image from the path to the thumbnails directory.
   *
   * @return    video thumbnail
   */
  private Bitmap getThumbnailFromPath() {
    String bitmapPath = metadata.getThumbnailPath();
    Bitmap thumbnail = BitmapFactory.decodeFile(bitmapPath);
    Log.d("DEBUG", "THUMBNAIL PATH IS: " + bitmapPath);
    return thumbnail;
  }

  /**
   * Get the thumbnail for this video.
   *
   * @return    video thumbnail
   */
  public Bitmap getThumbnail() {
    return this.thumbnail;
  }

}