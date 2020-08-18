package com.silviaodwyer.inversion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.silviaodwyer.inversion.utils.VideoFilters;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;

public class VideoThumbnailsRecyclerView extends RecyclerView.Adapter<VideoThumbnailsRecyclerView.ViewHolder> {

    private ArrayList<VideoThumbnail> data = new ArrayList<>();
    private ItemClickListener clickListener;
    private LayoutInflater inflater;
    private ArrayList<ImageMetadata> metaDataArray;
    private ImageUtils imageUtils;
    private Activity context;
    private MainApplication mainApplication;
    private VideoFilters videoFilters;

    public VideoThumbnailsRecyclerView(Activity context, ArrayList<VideoThumbnail> data, MainApplication mainApplication) {
        this.mainApplication = mainApplication;
        this.inflater = LayoutInflater.from(context);
        this.data.addAll(data);
        this.context = context;
        this.imageUtils = new ImageUtils(context);
        this.videoFilters = new VideoFilters(context);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoThumbnail videoThumbnail = data.get(position);
                videoFilters.filterVideo((GlFilter) videoThumbnail.getFilter(), mainApplication.getPlayerView());

            }

        });

        Bitmap bitmap = data.get(position).getBitmap();

        Glide
                .with(context)
                .load(bitmap)
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

    VideoThumbnail getItem(int id) {
        return data.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void update(ArrayList<VideoThumbnail> updatedThumbnails) {
        this.data.clear();
        this.data.addAll(updatedThumbnails);
        notifyDataSetChanged();
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }
}
