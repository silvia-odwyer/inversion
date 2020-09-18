package com.silviaodwyer.inversion;

import android.app.Application;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileUtils {
  private Context context;

  public FileUtils(Context ctx) {
    this.context = ctx;
  }

    public boolean isFilePresent(String fileName) {
      String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;

      // create a new file
      File file = new File(path);

      // return whether or not the file exists in memory
      return file.exists();
    }

  public String readFile(String fileName) {
    try {
      FileInputStream fileInputStream = context.openFileInput(fileName);
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }
      return stringBuilder.toString();
    } catch (IOException fileNotFound) {
      return null;
    }
  }

  public boolean writeFile(String fileName, String jsonString){
    try {
      FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      if (jsonString != null) {
        fileOutputStream.write(jsonString.getBytes());
      }
      fileOutputStream.close();
      return true;
    } catch (IOException fileNotFound) {
      return false;
    }
  }

  /**
   * Get the current time and date and
   * create a timestamp from this.
   *
   * @return timestamp
   */
  public static String createTimestamp() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return dateFormat.format(calendar.getTime());
  }

  public static void deleteDirectory(File dir, Context context) {
    if (dir.isDirectory())
    {
      String[] dir_children = dir.list();
      for (int i = 0; i < dir_children.length; i++)
      {
        File file = new File(dir, dir_children[i]);
        file.delete();
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
      }
    }
  }

  public void downloadVideo(String fileURL, File outputFile, Application application) {
    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileURL))
      .setTitle(outputFile.getName())
      .setDescription("Downloading file")
      .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
      .setDestinationUri(Uri.fromFile(outputFile))
      .setAllowedOverRoaming(true)
      .setAllowedOverMetered(true);
    DownloadManager downloadManager = (DownloadManager) application.getSystemService(application.DOWNLOAD_SERVICE);
    long downloadRes = downloadManager.enqueue(request);
  }

  public void deleteAllFilesInDirectory(File directory) {
    FileUtils fileUtils = new FileUtils(context);
    fileUtils.deleteDirectory(directory, context);
    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
  }


  public static String getFileFromAssets(Context context, String filename) {
    String jsonString = null;

    try {
      InputStream inputStream = context.getAssets().open(filename);
      int size = inputStream.available();

      // create buffer
      byte[] buffer = new byte[size];
      inputStream.read(buffer);

      // close
      inputStream.close();
      jsonString = new String(buffer, StandardCharsets.UTF_8);
    } catch (IOException exception) {
      exception.printStackTrace();
      return null;
    }

    return jsonString;
  }

  public String getPathFromUri (Uri uri) {
    String abs_path = null;
    String[] data = { MediaStore.MediaColumns.DATA };

    // get cursor
    Cursor cursor = context.getContentResolver().query(uri, data, null, null, null);

    if (cursor.moveToFirst()) {
      int col_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

      // get path as a string using the column index
      abs_path = cursor.getString(col_index);
    }
    cursor.close();
    return abs_path;
  }

  public String videoUriToPath(Uri uri, ContentResolver contentResolver) {
    String[] projection = {MediaStore.Video.Media.DATA};

    try (Cursor cursor = contentResolver.query(uri, projection, null, null, null)) {
      if (cursor != null) {
        int columnIndex = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
      } else
        return null;
    }
  }

}