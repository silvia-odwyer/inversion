package com.silviaodwyer.inversion;

import android.os.Environment;

import java.io.File;

public class VideoMetadata extends FileMetadata {
    private String originalVideoPath;

    public VideoMetadata(String name, String timestamp, String videoUrl) {
        super(name, timestamp, FileType.VIDEO);
        this.originalVideoPath = videoUrl;
    }

    public VideoMetadata(String videoUrl) {
        super(FileType.VIDEO);
        this.originalVideoPath = videoUrl;
    }

    public String getThumbnailPath() {
        File videoThumbnailsDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos/thumbnails");
        File file = new File(videoThumbnailsDirectory, getName() + ".png");
        return file.getAbsolutePath();
    }

    public String getOriginalVideoPath() {
        return originalVideoPath;
    }

}
