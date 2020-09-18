package com.silviaodwyer.inversion.video_filters;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.silviaodwyer.inversion.utils.FileUtils;
import com.silviaodwyer.inversion.VideoFilterMetadata;

import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<VideoFilterMetadata> popularFiltersMetadata = new ArrayList<>();

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

    public ArrayList<VideoFilterMetadata> getGlitchFiltersMetadata() {
        if (this.glitchFiltersMetadata.size() == 0) {
            JsonArray glitch_filter_array = filtersObj.get("glitch").getAsJsonArray();
            List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(glitch_filter_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
            this.glitchFiltersMetadata.addAll(videoFilterMetadata);
        }
        return glitchFiltersMetadata;
    }


    public ArrayList<VideoFilterMetadata> getWarmTintFiltersMetadata() {
        if (this.warmTintFiltersMetadata.size() == 0) {
            JsonArray filter_array = filtersObj.get("warm").getAsJsonArray();
            List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(filter_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
            this.warmTintFiltersMetadata.addAll(videoFilterMetadata);
        }
        return warmTintFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getPopularFiltersMetadata() {
        if (this.popularFiltersMetadata.size() == 0) {
            JsonArray filter_array = filtersObj.get("popular").getAsJsonArray();
            List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(filter_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
            this.popularFiltersMetadata.addAll(videoFilterMetadata);
        }
        return popularFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getColdTintFiltersMetadata() {
        if (this.coldTintFiltersMetadata.size() == 0) {
            JsonArray filter_array = filtersObj.get("cold").getAsJsonArray();
            List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(filter_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
            this.coldTintFiltersMetadata.addAll(videoFilterMetadata);        }
        return coldTintFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getColorGlitchFiltersMetadata() {
        if (this.colorGlitchFiltersMetadata.size() == 0) {
            JsonArray glitch_filter_array = filtersObj.get("color glitch").getAsJsonArray();
            List<VideoFilterMetadata> videoFilterMetadata = gson.fromJson(glitch_filter_array, new TypeToken<List<VideoFilterMetadata>>(){}.getType());
            this.colorGlitchFiltersMetadata.addAll(videoFilterMetadata);
        }
        return colorGlitchFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getGradientFiltersMetadata() {
        return gradientFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getVintageFiltersMetadata() {
        return vintageFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getDissolveFiltersMetadata() {
        return dissolveFiltersMetadata;
    }

    public ArrayList<VideoFilterMetadata> getColorBlendFiltersMetadata() {
        return colorBlendFiltersMetadata;
    }

}
