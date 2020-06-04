package com.silviaodwyer.inversion;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;

public class MainApplication extends Application {
  private String videoUrl;
  private ImageEditor imageEditorActivity;
  private Image image;
  private ArrayList<String> imageNames = new ArrayList<String>();
  private static String savedImagePathFilename = "saved_image_paths.json";
  private static String IMAGES_DIRECTORY = "imagesDirectory";

  public void setVideoUrl(String vidUrl) {
    this.videoUrl = vidUrl;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public ImageEditor getImageEditorActivity() {
    return imageEditorActivity;
  }

  /**
   * Returns the active image instance
   *
   * @return      image instance
   */
  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public void setImageEditorActivity(ImageEditor imageEditorActivity) {
    this.imageEditorActivity = imageEditorActivity;
  }

  /**
   * Returns the name of the directory in internal storage where saved images are stored
   *
   * @return      the image editor activity
   */
  public static String getImagesDirectory() {
    return IMAGES_DIRECTORY;
  }

  /**
   * Returns all names of images currently saved in internal storage
   *
   * @return      the image editor activity
   */
  public ArrayList<String> getSavedImageNames(Context context) {
    FileUtils fileUtils = new FileUtils(context);
    String FILENAME = getSavedImagePathFilename();
    ArrayList<String> savedImagePaths = new ArrayList<>();

    boolean isFilePresent = fileUtils.isFilePresent(FILENAME);
    if(isFilePresent) {
      String jsonString = fileUtils.readFile(FILENAME);

      savedImagePaths = new Gson().fromJson(jsonString, new TypeToken<List<String>>(){}.getType());
    }

    return savedImagePaths;
  }

  public void setImageNames(ArrayList<String> imageNames) {
    this.imageNames = imageNames;
  }

  /**
   * Returns the name of the file which contains the names of all images stored by the user
   *
   * @return      the name of the JSON file which contains saved image names as JSON
   */
  public String getSavedImagePathFilename() {
    return savedImagePathFilename;
  }

  public void addSavedImageName(String image_name, Context context) {
    imageNames = getSavedImageNames(context);
    imageNames.add(image_name);
  }

}
