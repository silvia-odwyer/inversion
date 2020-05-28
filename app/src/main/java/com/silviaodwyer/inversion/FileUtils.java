package com.silviaodwyer.inversion;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
}
