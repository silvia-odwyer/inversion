package com.silviaodwyer.inversion;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ImageMetadata {
  private String name;
  private String timestamp;

  public ImageMetadata(String name) {
    this.name = name;

  }

  public ImageMetadata() {
    this.name = generateImageName();
    this.timestamp = generateTimestamp();
  }

  /**
   * Get the timestamp for this image.
   *
   * @return image's timestamp
   */
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * Set the timestamp for this image.
   *
   */
  public void setTimestamp(String timestamp) {

    this.timestamp = timestamp;
  }

  /**
   * Get the current time and date and
   * create a timestamp from this.
   *
   * @return timestamp
   */
  public String generateTimestamp() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return dateFormat.format(calendar.getTime());
  }

  /**
   * Get the name for this image.
   *
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name for this image.
   *
   */
  public void setName(String name) {
    this.name = name;
  }

  public static String generateImageName() {
    Calendar calendar = Calendar.getInstance();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date = dateFormat.format(calendar.getTime());

    String image_name = "image_" + date + ".jpg";
    Log.d("DEBUG", "IMAGE NAME: " + image_name);
    return image_name;
  }

}
