package com.silviaodwyer.inversion;

import android.os.Environment;

import java.io.File;

public class VideoMetadata extends FileMetadata {
    public VideoMetadata(String name, String timestamp, String originalVideoPath) {
        super(name, timestamp, FileType.VIDEO, originalVideoPath);
    }

    public VideoMetadata(String originalVideoPath) {
        super(FileType.VIDEO, originalVideoPath);
    }

    public String getThumbnailPath() {
        File videoThumbnailsDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos/thumbnails");
        File file = new File(videoThumbnailsDirectory, getName() + ".png");
        return file.getAbsolutePath();
    }
}
