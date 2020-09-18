package com.silviaodwyer.inversion.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.silviaodwyer.inversion.models.ImageMetadata;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.image_filters.ImageFilterPacks;
import com.silviaodwyer.inversion.image_filters.ImageFilters;
import com.silviaodwyer.inversion.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class WeeklyEditedImagesRecyclerView extends RecyclerView.Adapter<WeeklyEditedImagesRecyclerView.ViewHolder> {

    private ArrayList<ImageMetadata> data;
    private ItemClickListener clickListener;
    private LayoutInflater inflater;
    private ArrayList<ImageMetadata> metaDataArray;
    private ImageUtils imageUtils;
    private Activity activity;
    private Activity context;
    private ImageFilterPacks imageFilterPacks;
    private MainApplication mainApplication;
    private ImageFilters imageFilters;
    private List<List<Object>> filters;

    public WeeklyEditedImagesRecyclerView(Activity context, ArrayList<ImageMetadata> data, MainApplication mainApplication) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.mainApplication = mainApplication;
        this.imageUtils = new ImageUtils(context);

        this.imageFilters = new ImageFilters(context);
        this.filters = imageFilterPacks.createVintageFilters();
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
        ImageMetadata metadata = data.get(position);

        Glide.with(context)
                .asBitmap()
                .load(metadata.getOriginalImagePath())
                .into(new CustomTarget<Bitmap>(250, 250) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageFilterPacks = new ImageFilterPacks(context, resource);
                        filters = imageFilterPacks.createPopularFilters();

                        GPUImage gpuImage = new GPUImage(context);
                        gpuImage.setImage(resource);
                        Random r = new Random();
                        int index = r.nextInt(filters.size());
                        gpuImage.setFilter((GPUImageFilter) filters.get(index).get(1));

                        holder.imageView.setImageBitmap(gpuImage.getBitmapWithFilterApplied());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
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

    ImageMetadata getItem(int id) {
        return data.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void update(ArrayList<ImageMetadata> updatedFiles) {
        this.data.clear();
        this.data = updatedFiles;
        notifyDataSetChanged();
    }
}
