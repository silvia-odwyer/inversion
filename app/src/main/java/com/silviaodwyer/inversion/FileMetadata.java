package com.silviaodwyer.inversion;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FileMetadata {
  enum FileType {
    IMAGE,
    VIDEO,
  }

  private String name;
  private String timestamp;
  private FileType fileType;

  public FileMetadata(String name, String timestamp, FileType fileType) {
    this.name = name;
    this.timestamp = timestamp;
    this.fileType = fileType;
  }

  public FileMetadata(FileType fileType) {
    this.timestamp = FileUtils.createTimestamp();
    this.fileType = fileType;
    this.name = generateName();
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

  public String generateName() {
    String file_name = "";
    switch(fileType) {
      case IMAGE:
        file_name = "image_" + this.timestamp;
        break;
      case VIDEO:
        file_name = "video_" + this.timestamp;
        break;
    }

    Log.d("DEBUG", "NAME GENERATED: " + file_name);
    return file_name;
  }

  public FileType getFileType() {
    return fileType;
  }

  public void setFileType(FileType fileType) {
    this.fileType = fileType;
  }

  public String getAbsolutePath() {
    String sub_dir = "";
    String name = "";

    switch(fileType) {
      case IMAGE:
        sub_dir = "images/";
        break;

      case VIDEO:
        sub_dir = "videos/";
    }
    File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/" + sub_dir);

    switch(fileType) {
      case IMAGE:
        name = getName() + ".png";
        break;
      case VIDEO:
        name = getName() + ".mp4";
    }
    File file = new File(directory, name);
    return file.getAbsolutePath();
  }

}
