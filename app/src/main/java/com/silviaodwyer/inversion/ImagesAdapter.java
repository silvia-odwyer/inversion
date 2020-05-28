package com.silviaodwyer.inversion;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends BaseAdapter {

  private final Context mContext;
  private final ArrayList<String> images = new ArrayList<String>();
  private static String FILENAME = "stored_images.json";
  private ArrayList<String> savedImagePaths = new ArrayList<>();

  public ImagesAdapter(Context context) {
    this.mContext = context;
    for (int i = 0; i < 30; i++) {
      String img_name = "Image" + i;
      images.add(img_name);
    }
  }

  @Override
  public int getCount() {
    return images.size();
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
    // get image path

    imageView.setImageResource(R.drawable.gradient);
    imageView.setMinimumWidth(30);
    imageView.setMinimumHeight(90);
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(mContext, ImageEditor.class);
        mContext.startActivity(intent);
      }
    });
    return imageView;
  }

  private void getSavedImages() {
    FileUtils fileUtils = new FileUtils(mContext);

    boolean isFilePresent = fileUtils.isFilePresent(FILENAME);

    // if the file exists
    if(isFilePresent) {
      String jsonString= fileUtils.readFile(FILENAME);

      savedImagePaths = new Gson().fromJson(jsonString, new TypeToken<List<String>>() {
      }.getType());
      Log.d("DEBUG", "File present");

    }
    else {
      // create a new file
      Log.d("DEBUG", "File not present");
    }
  }

}
