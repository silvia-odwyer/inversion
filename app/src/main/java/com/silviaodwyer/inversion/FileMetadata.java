package com.silviaodwyer.inversion;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FileMetadata {
  enum FileType {
    IMAGE,
    VIDEO,
  }

  private String name;
  private String timestamp;
  private FileType fileType;
  private String url;
  private String originalFileUrl;

  public FileMetadata(String name, String timestamp, FileType fileType, String originalFilePath) {
    this.name = name;
    this.timestamp = timestamp;
    this.fileType = fileType;
    this.url = getAbsolutePath();
    this.originalFileUrl = originalFilePath;
  }

  public FileMetadata(FileType fileType, String originalFilePath) {
    this.timestamp = FileUtils.createTimestamp();
    this.fileType = fileType;
    this.name = generateName();
    this.url = getAbsolutePath();
    this.originalFileUrl = originalFilePath;
  }

  public FileMetadata(FileType fileType, String url, String originalFilePath) {
    this.fileType = fileType;
    this.url = url;
    this.originalFileUrl = originalFilePath;
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
   * Get the image name.
   *
   */
  public String getName() {
    return name;
  }

  /**
   * Set the image name.
   *
   */
  public void setName(String name) {
    this.name = name;
  }

  private String generateName() {
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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

  public String getOriginalFileUrl() {
    return originalFileUrl;
  }

  public void setOriginalFileUrl(String originalFileUrl) {
    this.originalFileUrl = originalFileUrl;
  }


}
