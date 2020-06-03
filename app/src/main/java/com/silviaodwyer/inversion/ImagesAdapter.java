package com.silviaodwyer.inversion;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends BaseAdapter {

  private final Context mContext;
  private static String FILENAME;
  private MainApplication mainApplication;
  private ArrayList<String> savedImageNames = new ArrayList<>();

  public ImagesAdapter(Context context) {
    this.mContext = context;
    mainApplication = new MainApplication();
    FILENAME = mainApplication.getSavedImagePathFilename();
    this.getSavedImageNames();
  }

  @Override
  public int getCount() {
    return savedImageNames.size();
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

    ContextWrapper contextWrapper = new ContextWrapper(mContext);
    File directory = contextWrapper.getDir(mainApplication.getImagesDirectory(), Context.MODE_PRIVATE);
    String imageName = savedImageNames.get(position);

    ImageView imageView = new ImageView(mContext);

    try {
        File file = new File(directory, imageName);
        final Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        if (bitmap == null) {
          Log.d("DEBUG", "BITMAP IS NULL");
        }
        else {

          int maxHeight = 250;
          int maxWidth = 250;
          float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));
          // resize bitmap to thumbnail size
          Matrix matrix = new Matrix();
          matrix.postScale(scale, scale);

          final Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
          imageView.setImageBitmap(resultBitmap);
          imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              Intent intent = new Intent(Images.this, ImageEditor.class);
//              Image image = new Image(bitmap, mContext, mainApplication.getImageEditorActivity());
//              mainApplication.setImage(image);
//              mContext.startActivity(intent);
            }
          });
        }

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

    return imageView;
  }

  private void getSavedImageNames() {
    FileUtils fileUtils = new FileUtils(mContext);

    boolean isFilePresent = fileUtils.isFilePresent(FILENAME);

    // if the file exists
    if(isFilePresent) {
      String jsonString= fileUtils.readFile(FILENAME);

      savedImageNames = new Gson().fromJson(jsonString, new TypeToken<List<String>>() {
      }.getType());

      Log.d("DEBUG", "File present");

    }
    else {
      Log.d("DEBUG", "File not present");
    }
  }


}
