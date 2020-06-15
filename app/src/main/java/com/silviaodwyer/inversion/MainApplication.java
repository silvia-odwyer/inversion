package com.silviaodwyer.inversion;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jp.co.cyberagent.android.gpuimage.GPUImage;

public class MainApplication extends Application {
  private String videoUrl;
  private ImageEditor imageEditorActivity;
  private Image image;
  private ArrayList<ImageMetadata> imageMetaDataArrayList = new ArrayList<>();
  private static String savedImagePathFilename = "saved_image_paths.json";
  private static String IMAGES_DIRECTORY = "imagesDirectory";
  private static String IMAGE_EFFECTS_LIST = "image_effects_list.json";

  /**
   * Set the active video URL
   *
   */
  public void setVideoUrl(String vidUrl) {
    this.videoUrl = vidUrl;
  }

  /**
   * Returns the active video URL
   *
   * @return      active video URL
   */
  public String getVideoUrl() {
    return videoUrl;
  }

  /**
   * Returns an instance of the ImageEditor activity
   *
   * @return      instance of the ImageEditor activity
   */
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
  public ArrayList<ImageMetadata> getMetaDataArrayList(Context context) {
    FileUtils fileUtils = new FileUtils(context);
    String FILENAME = getSavedImagePathFilename();
    ArrayList<ImageMetadata> savedImageNames = new ArrayList<>();

    boolean isFilePresent = fileUtils.isFilePresent(FILENAME);
    String dir = context.getFilesDir().getAbsolutePath();
    Log.d("DEBUG", "Dir " + dir);
    if(isFilePresent) {
      String jsonString = fileUtils.readFile(FILENAME);

      savedImageNames = new Gson().fromJson(jsonString, new TypeToken<List<ImageMetadata>>(){}.getType());
    }

    Collections.reverse(savedImageNames);

    return savedImageNames;
  }

  /**
   * Returns the name of the file which contains the names of all images stored by the user
   *
   * @return      the name of the JSON file which contains saved image names as JSON
   */
  public String getSavedImagePathFilename() {
    return savedImagePathFilename;
  }

  /**
   * Add a new image name (for an image that was newly saved and was not
   * previously in the list) to the list of saved image names
   *
   * @param imageMetadata
   * @param context
   */
  public void addSavedImageMetadata(ImageMetadata imageMetadata, Context context) {
    imageMetaDataArrayList = getMetaDataArrayList(context);
    imageMetaDataArrayList.add(imageMetadata);
  }

  /**
   * Returns the name of the image effects list file.
   * The image effects list file is a JSON file containing a list of
   * all available image effects and filters, plus descriptions of each.
   *
   * @return      image instance
   */
  public static String getImageEffectsList() {
    return IMAGE_EFFECTS_LIST;
  }


}
