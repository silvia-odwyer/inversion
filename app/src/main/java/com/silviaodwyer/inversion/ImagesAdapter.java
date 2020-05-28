package com.silviaodwyer.inversion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends BaseAdapter {

  private final Context mContext;
  private static String FILENAME;
  private ArrayList<String> savedImagePaths = new ArrayList<>();

  public ImagesAdapter(Context context) {
    this.mContext = context;
    MainApplication mainApplication = new MainApplication();
    FILENAME = mainApplication.getSavedImagePathFilename();
    this.getSavedImages();
  }

  @Override
  public int getCount() {
    return savedImagePaths.size();
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
    String path = savedImagePaths.get(position);
    Uri imageUri = Uri.fromFile(new File(path));
    Log.d("DEBUG", imageUri.toString());

    try {
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);

//      Picasso.with().load(path).centerCrop().resize(200,200).into(imageView);

    } catch (IOException e) {
      e.printStackTrace();
    }

//    imageView.setImageURI(imageUri);
//    imageView.setMinimumWidth(30);
//    imageView.setMinimumHeight(90);
//    imageView.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        Intent intent = new Intent(mContext, ImageEditor.class);
//        mContext.startActivity(intent);
//      }
//    });
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
      Log.d("DEBUG", "File not present");
    }
  }

}
