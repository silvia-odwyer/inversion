package com.silviaodwyer.inversion;

import android.app.Application;
import android.net.Uri;

import jp.co.cyberagent.android.gpuimage.GPUImage;

public class MainApplication extends Application {
  private Uri imageUri;
  private String videoUrl;
  private ImageEditor imageEditorActivity;

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

  public ImageEditor getImageEditorActivity() {
    return imageEditorActivity;
  }

  public void setImageEditorActivity(ImageEditor imageEditorActivity) {
    this.imageEditorActivity = imageEditorActivity;
  }
}
