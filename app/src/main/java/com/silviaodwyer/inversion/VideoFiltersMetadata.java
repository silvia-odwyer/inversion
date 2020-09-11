package com.silviaodwyer.inversion;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silviaodwyer.inversion.image_filters.ImageFilterMetadata;

import java.util.ArrayList;
import java.util.List;

public class VideoFiltersMetadata {
    private ArrayList<VideoFilterMetadata> gradientFiltersMetadata = new ArrayList<VideoFilterMetadata>();
    private ArrayList<VideoFilterMetadata> vintageFiltersMetadata = new ArrayList<VideoFilterMetadata>();
    private ArrayList<VideoFilterMetadata> gradientGrayscaleFiltersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> dissolveFiltersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> colorBlendFiltersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> filtersMetadata = new ArrayList<>();
    private Context context;

    public VideoFiltersMetadata(Context context) {
         this.context = context;
         this.readFiltersFile();
    }

    public void readFiltersFile() {
        String jsonString = FileUtils.getFileFromAssets(context, "filters.json");

        Gson gson = new Gson();

        List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(jsonString, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
        filtersMetadata.addAll(videoFilterMetadata);
        this.vintageFiltersMetadata.addAll(videoFilterMetadata);

    }

    public ArrayList<VideoFilterMetadata> getGradientFiltersMetadata() {
        return gradientFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getVintageFiltersMetadata() {
        return vintageFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getGradientGrayscaleFiltersMetadata() {
        return gradientGrayscaleFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getDissolveFiltersMetadata() {
        return dissolveFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getColorBlendFiltersMetadata() {
        return colorBlendFiltersMetadata;
    }
}
