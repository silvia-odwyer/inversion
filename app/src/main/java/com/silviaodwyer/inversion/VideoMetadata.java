package com.silviaodwyer.inversion;

import android.os.Environment;

import java.io.File;

public class VideoMetadata extends FileMetadata {

    public VideoMetadata(String name, String timestamp) {
        super(name, timestamp, FileType.VIDEO);
    }

    public VideoMetadata() {
      super(FileType.VIDEO);
    }

    public String getThumbnailPath() {
        File videoThumbnailsDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos/thumbnails");
        File file = new File(videoThumbnailsDirectory, getName() + ".png");
        return file.getAbsolutePath();
    }
}
