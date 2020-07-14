package com.silviaodwyer.inversion;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeeklyEditedImagesRecyclerView extends RecyclerView.Adapter<WeeklyEditedImagesRecyclerView.ViewHolder> {

    private ArrayList<FileMetadata> data;
    private ItemClickListener clickListener;
    private LayoutInflater inflater;
    private ArrayList<FileMetadata> metaDataArray;
    private ImageUtils imageUtils;
    private Activity activity;
    private Activity context;
    private MainApplication mainApplication;

    public WeeklyEditedImagesRecyclerView(Activity context, ArrayList<FileMetadata> data, MainApplication mainApplication) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.mainApplication = mainApplication;
        this.imageUtils = new ImageUtils(context);
    }

    @Override
    @NonNull
    public WeeklyEditedImagesRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_image, parent, false);
        return new ViewHolder(view);
    }

    // binds the bitmap to the ImageView
    @Override
    public void onBindViewHolder(@NonNull WeeklyEditedImagesRecyclerView.ViewHolder holder, int position) {
        FileMetadata metadata = data.get(position);

        Glide
                .with(context)
                .load(metadata.getUrl())
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

    FileMetadata getItem(int id) {
        return data.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void update(ArrayList<FileMetadata> updatedFiles) {
        this.data.clear();
        this.data = updatedFiles;
        notifyDataSetChanged();
    }
}
