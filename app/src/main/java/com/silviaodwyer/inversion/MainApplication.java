package com.silviaodwyer.inversion;

import android.app.Application;
import android.net.Uri;

public class MainApplication extends Application {
  private Uri imageUri;
  private String videoUrl;

  public Uri getImageUri() {
    return imageUri;
  }

  public void setImageUri(Uri imageUri) {
    this.imageUri = imageUri;
  }

  public void setVideoUrl(String vidUrl) {
    this.videoUrl = vidUrl;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

}
