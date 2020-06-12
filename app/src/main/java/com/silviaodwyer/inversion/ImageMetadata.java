package com.silviaodwyer.inversion;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImageMetadata {
  private String name;

  public ImageMetadata(String name) {
    this.name = name;
  }

  public ImageMetadata() {
    this.name = generateImageName();
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
