package com.silviaodwyer.inversion;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
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

public class VideosRecyclerView extends RecyclerView.Adapter<VideosRecyclerView.ViewHolder> {

  private ArrayList<VideoMetadata> data;
  private ItemClickListener clickListener;
  private LayoutInflater inflater;
  private Context context;
  private MainApplication mainApplication;
  private ImageUtils imageUtils;

  VideosRecyclerView(Context context, ArrayList<VideoMetadata> data, MainApplication mainApplication) {
    this.inflater = LayoutInflater.from(context);
    this.data = data;
    this.context = context;
    this.mainApplication = mainApplication;
  }

  @Override
  @NonNull
  public VideosRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.recyclerview_image, parent, false);
    return new ViewHolder(view);
  }

  // binds the bitmap to the ImageView
  @Override
  public void onBindViewHolder(@NonNull VideosRecyclerView.ViewHolder holder, int position) {
    File inversionDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos/thumbnails");

    File file = new File(inversionDirectory, data.get(position).getName());

    Glide
      .with(context)
      .load(file.getAbsolutePath())
      .apply(new RequestOptions().override(450, 450))
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
        Intent intent = new Intent(context, VideoEditor.class);
        VideoMetadata metadata = data.get(getAdapterPosition());
        intent.putExtra("videoPath", metadata.getAbsolutePath());

        Video video = new Video(metadata);

        mainApplication.setVideo(video);
        context.startActivity(intent);
      }
    }

  }

  FileMetadata getItem(int id) {
    return data.get(id);
  }

  void setClickListener(ItemClickListener itemClickListener) {
    this.clickListener = itemClickListener;
  }

  public interface ItemClickListener {
    void onItemClick(View view, int position);
  }
}
