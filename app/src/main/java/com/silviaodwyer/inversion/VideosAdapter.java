package com.silviaodwyer.inversion;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.silviaodwyer.inversion.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VideosAdapter extends BaseAdapter {

  private final Context mContext;
  private final ArrayList<String> videos = new ArrayList<String>();

  public VideosAdapter(Context context) {
    this.mContext = context;
    initVideos();
  }

  private void initVideos() {
    for (int i = 0; i < 20; i++) {
      String vid_name = "Video_" + i;
      videos.add(vid_name);

      //TODO logic for loading video images should be added here.
    }
  }

  @Override
  public int getCount() {
    return videos.size();
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageView imageView = new ImageView(mContext);
    imageView.setImageResource(R.drawable.gradient);
    imageView.setMinimumWidth(40);
    imageView.setMinimumHeight(100);
    return imageView;
  }

}
