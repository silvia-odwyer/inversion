package com.silviaodwyer.inversion;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class ImagesRecyclerView extends RecyclerView.Adapter<ImagesRecyclerView.ViewHolder> {

  private ArrayList<Bitmap> data;
  private ItemClickListener clickListener;
  private LayoutInflater inflater;
  private Context context;
  private MainApplication mainApplication;
  private ArrayList<ImageMetadata> metaDataArray;
  private ImageUtils imageUtils;

  ImagesRecyclerView(Context context, ArrayList<Bitmap> data, MainApplication mainApplication, ArrayList<ImageMetadata> metadata) {
    this.inflater = LayoutInflater.from(context);
    this.data = data;
    this.context = context;
    this.metaDataArray = metadata;
    this.mainApplication = mainApplication;
    this.imageUtils = new ImageUtils(context);
  }

  @Override
  @NonNull
  public ImagesRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.recyclerview_image, parent, false);
    return new ViewHolder(view);
  }

  // binds the bitmap to the ImageView
  @Override
  public void onBindViewHolder(@NonNull ImagesRecyclerView.ViewHolder holder, int position) {
    Bitmap bitmap = data.get(position);

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
        Intent intent = new Intent(context, ImageEditor.class);
        ImageMetadata metadata = metaDataArray.get(getAdapterPosition());
        Image image = new Image(data.get(getAdapterPosition()), context, mainApplication.getImageEditorActivity(), metadata);
        mainApplication.setImage(image);
        context.startActivity(intent);
      }
    }

  }

  Bitmap getItem(int id) {
    return data.get(id);
  }

  void setClickListener(ItemClickListener itemClickListener) {
    this.clickListener = itemClickListener;
  }

  public interface ItemClickListener {
    void onItemClick(View view, int position);
  }
}
