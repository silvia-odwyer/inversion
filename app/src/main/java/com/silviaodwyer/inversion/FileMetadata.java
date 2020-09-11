package com.silviaodwyer.inversion;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FileMetadata {
  public enum FileType {
    IMAGE,
    VIDEO,
  }

  private String name;
  private String timestamp;
  private FileType fileType;
  private String url;

  public FileMetadata(String name, String timestamp, FileType fileType) {
    this.name = name;
    this.timestamp = timestamp;
    this.fileType = fileType;
    this.url = getAbsolutePath();
  }

  public FileMetadata(FileType fileType) {
    this.timestamp = FileUtils.createTimestamp();
    this.fileType = fileType;
    this.name = generateName();
    this.url = getAbsolutePath();
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
   * Get the file name.
   *
   * @return image's name
   */
  public String getName() {
    return name;
  }

  /**
   * Set the file name.
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


  /**
   * Get the file type.
   *
   * @return file's type, either image or video
   */
  public FileType getFileType() {
    return fileType;
  }

  /**
   * Set the file type.
   *
   */
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
