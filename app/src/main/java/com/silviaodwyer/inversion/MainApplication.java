package com.silviaodwyer;

import android.app.Application;
import android.net.Uri;

public class MainApplication extends Application {
  private Uri imageUri;

  public Uri getImageUri() {
    return imageUri;
  }

  public void setImageUri(Uri imageUri) {
    this.imageUri = imageUri;
  }
}
