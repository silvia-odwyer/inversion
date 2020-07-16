package com.silviaodwyer.inversion;

import android.net.Uri;

public class ImageMetadata extends FileMetadata {
    private Uri originalImageUri;

    public ImageMetadata(String name, String timestamp, Uri originalImageUri) {
        super(name, timestamp, FileType.IMAGE);
        this.originalImageUri = originalImageUri;
    }

    public ImageMetadata(Uri originalImageUri) {
        super(FileType.IMAGE);
        this.originalImageUri = originalImageUri;
    }

    public Uri getOriginalImageUri() {
        return originalImageUri;
    }

    public void setOriginalImageUri(Uri originalImageUri) {
        this.originalImageUri = originalImageUri;
    }

}
