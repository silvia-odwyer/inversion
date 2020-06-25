package com.silviaodwyer.inversion;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.daasuu.epf.EPlayerView;
import androidx.core.app.ActivityCompat;

public class MainApplication extends Application {
  private String videoUrl;
  private Video video;
  private ImageEditor imageEditorActivity;
  private Image image;
  private EPlayerView playerView;
  private Bitmap videoThumbnail;
  private ArrayList<FileMetadata> fileMetaDataArrayList = new ArrayList<>();
  private VideoEditor videoEditorActivity;
  private static String savedImageMetadataFilename = "saved_image_paths.json";
  private static String savedVideoMetadataFilename = "saved_video_paths.json";
  private static String IMAGES_DIRECTORY = "imagesDirectory";
  private static String IMAGE_EFFECTS_LIST = "image_effects_list.json";


  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video, Bitmap videoThumbnail) {
    this.video = video;
    this.videoThumbnail = videoThumbnail;
  }

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
  public ArrayList<FileMetadata> getSavedImageMetadata(Context context) {
    String filename = getSavedImageMetadataFilename();
    FileUtils fileUtils = new FileUtils(context);

    ArrayList<FileMetadata> metadata = new ArrayList<>();
    boolean isFilePresent = fileUtils.isFilePresent(filename);
    if(isFilePresent) {
      String jsonString = fileUtils.readFile(filename);
      metadata = new Gson().fromJson(jsonString, new TypeToken<List<FileMetadata>>(){}.getType());
      Log.d("DEBUG", "SAVED METADATA LENGTH: " + metadata.size());
    }

    Collections.reverse(metadata);

    return metadata;
  }

  public ArrayList<VideoMetadata> getSavedVideoMetadata(Context context) {
    String filename = getSavedVideoMetadataFilename();
    FileUtils fileUtils = new FileUtils(context);

    ArrayList<VideoMetadata> metadata = new ArrayList<>();

    boolean isFilePresent = fileUtils.isFilePresent(filename);

    if(isFilePresent) {
      String jsonString = fileUtils.readFile(filename);

      metadata = new Gson().fromJson(jsonString, new TypeToken<List<VideoMetadata>>(){}.getType());

      Log.d("DEBUG", "SAVED METADATA LENGTH: " + metadata.size());
    }

    Collections.reverse(metadata);

    return metadata;
  }

  /**
   * Returns the name of the file which contains the names of all images stored by the user
   *
   * @return      the name of the JSON file which contains saved image names as JSON
   */
  public String getSavedImageMetadataFilename() {
    return savedImageMetadataFilename;
  }

  /**
   * Returns the name of the file which contains the names of all videos stored by the user
   *
   * @return      the name of the JSON file which contains saved video names as JSON
   */
  public String getSavedVideoMetadataFilename() {
    return savedVideoMetadataFilename;
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

  public void requestPermissions(Activity activity) {
    ActivityCompat.requestPermissions(activity,
      new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
      1);
  }

  public void saveImageMetadata(FileMetadata metadata) {
    String FILENAME = "";
    FileUtils fileUtils = new FileUtils(getApplicationContext());

    ArrayList<FileMetadata> savedFileMetadata = getSavedImageMetadata(getApplicationContext());
    savedFileMetadata.add(metadata);
    String json = new Gson().toJson(savedFileMetadata);

    // save
    fileUtils.writeFile(FILENAME, json);
  }

  public void saveVideoMetadata(VideoMetadata metadata) {
    String FILENAME = "";
    FileUtils fileUtils = new FileUtils(getApplicationContext());

    ArrayList<VideoMetadata> savedVideoMetadata = getSavedVideoMetadata(getApplicationContext());
    savedVideoMetadata.add(metadata);
    String json = new Gson().toJson(savedVideoMetadata);

    // save
    fileUtils.writeFile(FILENAME, json);
  }

  public void saveVideo(Video video) {
    // save bitmap as thumbnail in thumbnails directory

    saveVideoMetadata(video.getMetadata());
  }

  public void deleteAllMetadata(FileMetadata.FileType fileType) {
    FileUtils fileUtils = new FileUtils(getApplicationContext());
    ArrayList<FileMetadata> savedFileMetadata = new ArrayList<>();
    String FILENAME = null;
    switch(fileType){
      case IMAGE:
        FILENAME = getSavedImageMetadataFilename();
      case VIDEO:
        FILENAME = getSavedVideoMetadataFilename();
    }

    String json = new Gson().toJson(savedFileMetadata);
    Log.d("DEBUG", "Metadata now: " + savedFileMetadata.toString());

    // save
    fileUtils.writeFile(FILENAME, json);
  }

  /**
   * Returns an instance of the VideoEditor activity
   *
   * @return      instance of the VideoEditor activity
   */
  public VideoEditor getVideoEditorActivity() {
    return videoEditorActivity;
  }

  /**
   * Returns an instance of the playerview used by the video player.
   *
   * @return      instance of the video player's PlayerView
   */
  public EPlayerView getPlayerView() {return this.playerView;}

  /**
   * Set the playerview instance of the video player
   *
   */
  public void setPlayerView(EPlayerView playerView) {this.playerView = playerView;}

  public Bitmap getVideoThumbnail() {
    return this.videoThumbnail;
  }

  public void setVideoThumbnail(Bitmap thumbnail) {
    this.videoThumbnail = thumbnail;
  }

}
