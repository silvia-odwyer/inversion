package com.silviaodwyer.inversion;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.silviaodwyer.inversion.image_filters.ImageFilterMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VideoFilterPacks {
    private ArrayList<VideoFilterMetadata> gradientFiltersMetadata = new ArrayList<VideoFilterMetadata>();
    private ArrayList<VideoFilterMetadata> vintageFiltersMetadata = new ArrayList<VideoFilterMetadata>();
    private ArrayList<VideoFilterMetadata> gradientGrayscaleFiltersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> dissolveFiltersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> colorBlendFiltersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> glitchFiltersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> filtersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> colorGlitchFiltersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> warmTintFiltersMetadata = new ArrayList<>();
    private ArrayList<VideoFilterMetadata> coldTintFiltersMetadata = new ArrayList<>();

    private JsonObject filtersObj;
    private Context context;
    private Gson gson;

    public VideoFilterPacks(Context context) {
         this.context = context;
         this.readFiltersFile();
    }

    public void readFiltersFile() {
        String jsonString = FileUtils.getFileFromAssets(context, "filters.json");

        gson = new Gson();

        filtersObj = new Gson().fromJson(jsonString, JsonObject.class);
        JsonArray vintage_array = filtersObj.get("vintage").getAsJsonArray();

        List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(vintage_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
        filtersMetadata.addAll(videoFilterMetadata);
        this.vintageFiltersMetadata.addAll(videoFilterMetadata);
    }

    public void initGlitchFiltersMetadata() {
        JsonArray glitch_filter_array = filtersObj.get("glitch").getAsJsonArray();
        List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(glitch_filter_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
        this.glitchFiltersMetadata.addAll(videoFilterMetadata);

    }

    public void initColorGlitchFiltersMetadata() {
        JsonArray glitch_filter_array = filtersObj.get("color glitch").getAsJsonArray();
        List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(glitch_filter_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
        this.colorGlitchFiltersMetadata.addAll(videoFilterMetadata);
    }

    private void initWarmTintFiltersMetadata() {
        JsonArray glitch_filter_array = filtersObj.get("warm").getAsJsonArray();
        List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(glitch_filter_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
        this.warmTintFiltersMetadata.addAll(videoFilterMetadata);
    }

    private void initColdTintFiltersMetadata() {
        JsonArray glitch_filter_array = filtersObj.get("cold").getAsJsonArray();
        List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(glitch_filter_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
        this.coldTintFiltersMetadata.addAll(videoFilterMetadata);
    }

    public ArrayList<VideoFilterMetadata> getWarmTintFiltersMetadata() {
        if (this.warmTintFiltersMetadata.size() == 0) {
            this.initWarmTintFiltersMetadata();
        }
        return warmTintFiltersMetadata;
    }
    public ArrayList<VideoFilterMetadata> getColdTintFiltersMetadata() {
        if (this.coldTintFiltersMetadata.size() == 0) {
            this.initColdTintFiltersMetadata();
        }
        return coldTintFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getGlitchFiltersMetadata() {
        if (this.glitchFiltersMetadata.size() == 0) {
            this.initGlitchFiltersMetadata();
        }
        return glitchFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getColorGlitchFiltersMetadata() {
        if (this.colorGlitchFiltersMetadata.size() == 0) {
            this.initColorGlitchFiltersMetadata();
        }
        return colorGlitchFiltersMetadata;
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

    public ArrayList<VideoFilterMetadata> getFiltersMetadata() {
        return filtersMetadata;
    }
}
