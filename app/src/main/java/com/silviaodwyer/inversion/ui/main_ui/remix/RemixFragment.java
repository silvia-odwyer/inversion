package com.silviaodwyer.inversion.ui.main_ui.remix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.silviaodwyer.inversion.models.Image;
import com.silviaodwyer.inversion.models.ImageMetadata;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.adapters.WeeklyEditedImagesRecyclerView;
import com.silviaodwyer.inversion.ui.image_editor.ImageEditor;
import com.silviaodwyer.inversion.utils.FileUtils;
import com.silviaodwyer.inversion.utils.ImageUtils;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class RemixFragment extends Fragment {
  private RecyclerView recyclerView;
  private View root;
  private WeeklyEditedImagesRecyclerView adapter;
  private MainApplication mainApplication;
  private Context context;
  private static int RESULT_LOAD_IMG = 7;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    root = inflater.inflate(R.layout.fragment_remix, container, false);
    Activity activity = getActivity();
    mainApplication = ((MainApplication) activity.getApplication());
    context = activity.getApplicationContext();

    this.initRecyclerView(mainApplication.getSavedImageMetadata(context));
    this.initUploadImageBtn();
    return root;
  }

  public void initUploadImageBtn() {

    Button uploadImageBtn = root.findViewById(R.id.upload_img_remix_btn);
    uploadImageBtn.setVisibility(View.VISIBLE);

    uploadImageBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
      }
    });
  }


  @Override
  public void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);
    Log.d("DEBUG", "REQUEST CODE: " + reqCode);
    if (resultCode == RESULT_OK) {
      if (reqCode == RESULT_LOAD_IMG) {
        startImageActivity(data);
      }
    }
  }


  private void startImageActivity(Intent data) {
    final Uri imageUri = data.getData();
    ImageUtils imageUtils = new ImageUtils(context);
    FileUtils fileUtils = new FileUtils(context);

    // set the image attribute for the application,
    String abs_path = fileUtils.getPathFromUri(imageUri);
    ImageMetadata metadata = new ImageMetadata(abs_path);
    Bitmap bitmap = imageUtils.imageUriToBitmap(imageUri);

    // Create new image and set it in MainApplication
    Image image = new Image(bitmap, context, mainApplication.getImageEditorActivity(), metadata);
    mainApplication.setImage(image);
    Intent intent = new Intent(getActivity(), ImageEditor.class);

    startActivity(intent);
  }


  private void initRecyclerView(ArrayList<ImageMetadata> imageMetadata) {
    recyclerView = root.findViewById(R.id.imgs_recycler_view);
    int numberOfColumns = 3;
    recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
    adapter = new WeeklyEditedImagesRecyclerView(getActivity(), imageMetadata, mainApplication);

    recyclerView.setAdapter(adapter);
  }
}
