package com.silviaodwyer.inversion;

import android.content.Context;
import java.io.File;

public class FileUtils {
    public boolean isFilePresent(Context ctx, String fileName) {
      String path = ctx.getFilesDir().getAbsolutePath() + "/" + fileName;

      // create a new file
      File file = new File(path);

      // return whether or not the file exists in memory
      return file.exists();
    }
}
