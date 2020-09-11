package com.silviaodwyer.inversion;

public class VideoFilterMetadata {
    private String name;
    private String url;
    private String description;

    public VideoFilterMetadata(String filterName, String filterUrl, String description) {
        this.name = filterName;
        this.url = filterUrl;
        this.description = description;
    }

    public String getFilterName() {
        return name;
    }

    public String getFilterUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}