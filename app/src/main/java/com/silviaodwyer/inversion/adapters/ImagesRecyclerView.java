package com.silviaodwyer.inversion.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.silviaodwyer.inversion.models.Image;
import com.silviaodwyer.inversion.models.ImageMetadata;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.ui.image_editor.ImageEditor;
import com.silviaodwyer.inversion.utils.ImageUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.cyberagent.android.gpuimage.GPUImage;

public class ImagesRecyclerView extends RecyclerView.Adapter<ImagesRecyclerView.ViewHolder> {

  private ArrayList<ImageMetadata> data;
  private ItemClickListener clickListener;
  private LayoutInflater inflater;
  private Activity context;
  private MainApplication mainApplication;
  private ArrayList<ImageMetadata> metaDataArray;
  private ImageUtils imageUtils;
  private Activity activity;
  private boolean isCentreCrop;

  public ImagesRecyclerView(Activity context, ArrayList<ImageMetadata> data, MainApplication mainApplication, boolean isCentreCrop) {
    this.inflater = LayoutInflater.from(context);
    this.data = data;
    this.context = context;
    this.mainApplication = mainApplication;
    this.imageUtils = new ImageUtils(context);
    this.isCentreCrop = isCentreCrop;
  }

  public void updateRecyclerView(ArrayList<ImageMetadata> updatedFiles) {
    this.data.clear();
    this.data = updatedFiles;
    notifyDataSetChanged();
  }

  @Override
  @NonNull
  public ImagesRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.recyclerview_image, parent, false);
    if (isCentreCrop) {
        ImageView imageView = view.findViewById(R.id.image_thumbnail);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
    return new ViewHolder(view);
  }

  // binds the bitmap to the ImageView
  @Override
  public void onBindViewHolder(@NonNull ImagesRecyclerView.ViewHolder holder, int position) {

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        long startTime = System.nanoTime();
        mainApplication.setStartTime(startTime);
        Intent intent = new Intent(context, ImageEditor.class);
        // ActivityOptions options = ActivityOptions
        //.makeSceneTransitionAnimation(activity, view, "robot");

        ImageMetadata metadata = data.get(position);
        Image image = ImageUtils.getImageFromFilename(metadata, context, mainApplication);
        mainApplication.setImage(image);
        GPUImage gpuImage = new GPUImage(context);
        mainApplication.setGpuImage(gpuImage);
        context.startActivity(intent);
      }

    });
    ImageMetadata metadata = data.get(position);
    Log.d("DEBUG", "Metadata is: " + metadata.toString());

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

  // Recycles views when they are being scrolled off the screen
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
}
