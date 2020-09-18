package com.silviaodwyer.inversion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.silviaodwyer.inversion.utils.ImageUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class ImageThumbnailsRecyclerView extends RecyclerView.Adapter<ImageThumbnailsRecyclerView.ViewHolder> {

    private ArrayList<ImageThumbnail> data = new ArrayList<>();
    private ItemClickListener clickListener;
    private LayoutInflater inflater;
    private ArrayList<ImageMetadata> metaDataArray;
    private ImageUtils imageUtils;
    private Activity activity;
    private Activity context;
    private MainApplication mainApplication;
    private GPUImageView gpuImageView;

    public ImageThumbnailsRecyclerView(Activity context, ArrayList<ImageThumbnail> data, MainApplication mainApplication, GPUImageView gpuImageView) {
        this.mainApplication = mainApplication;
        this.inflater = LayoutInflater.from(context);
        this.data.addAll(data);
        this.context = context;
        this.imageUtils = new ImageUtils(context);
        this.gpuImageView = gpuImageView;
    }

    @Override
    @NonNull
    public ImageThumbnailsRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_imagethumbnail, parent, false);
        return new ViewHolder(view);
    }

    // binds the bitmap to the ImageView
    @Override
    public void onBindViewHolder(@NonNull ImageThumbnailsRecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageThumbnail imageThumbnail = data.get(position);
                mainApplication.filterImage(imageThumbnail);
                mainApplication.getImage().getMetaData().setAppliedFilter(imageThumbnail.getFilterName());
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

    ImageThumbnail getItem(int id) {
        return data.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void update(ArrayList<ImageThumbnail> updatedThumbnails) {
        this.data.clear();
        this.data.addAll(updatedThumbnails);
        notifyDataSetChanged();
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }
}
