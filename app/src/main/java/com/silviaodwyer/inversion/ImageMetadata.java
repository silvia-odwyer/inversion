package com.silviaodwyer.inversion;

import android.net.Uri;
import java.util.ArrayList;

public class ImageMetadata extends FileMetadata {
    private String originalImagePath;
    private String appliedFilter;

    public ImageMetadata(String name, String timestamp, String originalImagePath) {
        super(name, timestamp, FileType.IMAGE);
        this.appliedFilter = "";
        this.originalImagePath = originalImagePath;
    }

    public ImageMetadata(String originalImagePath) {
        super(FileType.IMAGE);
        this.appliedFilter = "";
        this.originalImagePath = originalImagePath;
    }

    public String getOriginalImagePath() {
        return originalImagePath;
    }

    public String getAppliedFilter() {
        return appliedFilter;
    }

    public void setAppliedFilter(String appliedFilter) {
        this.appliedFilter = appliedFilter;
    }

    @Override
    public String toString() {
        return "ImageMetadata{" +
                "originalImagePath='" + originalImagePath + '\'' +
                ", appliedFilters=" + appliedFilter +
                '}';
    }
}
