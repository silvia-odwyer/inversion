package com.silviaodwyer.inversion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.silviaodwyer.inversion.ui.VideoEditor;
import com.silviaodwyer.inversion.utils.ImageUtils;
import com.silviaodwyer.inversion.video_filters.VideoFilters;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoThumbnailsRecyclerView extends RecyclerView.Adapter<VideoThumbnailsRecyclerView.ViewHolder> {

    private ArrayList<VideoFilterMetadata> data = new ArrayList<>();
    private ItemClickListener clickListener;
    private LayoutInflater inflater;
    private ArrayList<ImageMetadata> metaDataArray;
    private ImageUtils imageUtils;
    private Activity context;
    private MainApplication mainApplication;
    private VideoFilters videoFilters;

    private GPUPlayerView videoPlayerView;

    public VideoThumbnailsRecyclerView(Activity context, ArrayList<VideoFilterMetadata> data, MainApplication mainApplication, GPUPlayerView videoPlayerView) {
        this.mainApplication = mainApplication;
        this.inflater = LayoutInflater.from(context);
        this.data.addAll(data);
        this.context = context;
        this.imageUtils = new ImageUtils(context);
        this.videoFilters = new VideoFilters(context);
        this.videoPlayerView = videoPlayerView;
    }

    @Override
    @NonNull
    public VideoThumbnailsRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_imagethumbnail, parent, false);
        return new ViewHolder(view);
    }

    // binds the bitmap to the ImageView
    @Override
    public void onBindViewHolder(@NonNull VideoThumbnailsRecyclerView.ViewHolder holder, int position) {
        String videoFilterName = data.get(position).getFilterName();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoFilters.filterVideo(videoFilterName, videoPlayerView);

                if (context instanceof VideoEditor) {
                    ((VideoEditor)context).setActiveVideoFilterName(videoFilterName);
                }
            }

        });

        Glide
                .with(context)
                .load(data.get(position).getFilterUrl())
                .apply(new RequestOptions().override(250, 250))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    VideoFilterMetadata getItem(int id) {
        return data.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void update(ArrayList<VideoFilterMetadata> updatedThumbnails) {
        this.data.clear();
        this.data.addAll(updatedThumbnails);
        notifyDataSetChanged();
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }
}
