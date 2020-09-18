package com.silviaodwyer.inversion.models;

import android.os.Environment;

import com.silviaodwyer.inversion.models.FileMetadata;

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
