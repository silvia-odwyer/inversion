package com.silviaodwyer.inversion.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.silviaodwyer.inversion.models.FileMetadata;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.models.Video;
import com.silviaodwyer.inversion.models.VideoMetadata;
import com.silviaodwyer.inversion.ui.VideoEditor;
import com.silviaodwyer.inversion.utils.ImageUtils;

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

  public VideosRecyclerView(Context context, ArrayList<VideoMetadata> data, MainApplication mainApplication) {
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

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos");

        String name = data.get(position).getName() + ".mp4";

        Intent intent = new Intent(context, VideoEditor.class);
        VideoMetadata metadata = data.get(position);
        Video video = new Video(metadata);

//        String videoUrl = String.valueOf(Uri.fromFile(file));
        String videoUrl = video.getMetadata().getOriginalVideoPath();
        intent.putExtra("videoUrl", videoUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mainApplication.setVideo(video);
        context.startActivity(intent);
      }
    });

    File inversionDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos/thumbnails");

    File file = new File(inversionDirectory, data.get(position).getName() + ".png");

    Glide
      .with(context)
      .load(file.getAbsolutePath())
      .apply(new RequestOptions().override(200, 200))
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
      Log.d("DEBUG", "VID CLICKED");

      if (clickListener != null) {
        clickListener.onItemClick(view, getAdapterPosition());

        File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos");
        Log.d("DEBUG", "VIDEO CLICKED");
        String name = data.get(getAdapterPosition()).getName() + ".mp4";
        File file = new File(directory, name);

        Intent intent = new Intent(context, VideoEditor.class);
        VideoMetadata metadata = data.get(getAdapterPosition());
        Video video = new Video(metadata);

        String videoUrl = String.valueOf(Uri.fromFile(file));
        intent.putExtra("videoUrl", videoUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mainApplication.setVideo(video);
        context.startActivity(intent);
      }
    }

  }

  public void updateRecyclerView(ArrayList<VideoMetadata> updatedFiles) {
    this.data.clear();
    this.data = updatedFiles;
    notifyDataSetChanged();
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
}
