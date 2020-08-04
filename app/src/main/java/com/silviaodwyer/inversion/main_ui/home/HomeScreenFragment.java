package com.silviaodwyer.inversion.main_ui.home;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silviaodwyer.inversion.EffectDetail;
import com.silviaodwyer.inversion.FileUtils;
import com.silviaodwyer.inversion.Image;
import com.silviaodwyer.inversion.ImageEditor;
import com.silviaodwyer.inversion.FileMetadata;
import com.silviaodwyer.inversion.ImageFilterMetadata;
import com.silviaodwyer.inversion.ImageMetadata;
import com.silviaodwyer.inversion.ImageUtils;
import com.silviaodwyer.inversion.Images;
import com.silviaodwyer.inversion.ImagesRecyclerView;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.Shop;
import com.silviaodwyer.inversion.Video;
import com.silviaodwyer.inversion.VideoEditor;
import com.silviaodwyer.inversion.VideoMetadata;
import com.silviaodwyer.inversion.Videos;
import com.silviaodwyer.inversion.VideosRecyclerView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

public class HomeScreenFragment extends Fragment {
  private TextView viewImages;
  private TextView viewVideos;
  private String videoUrl;
  private Video video;
  private View root;
  private Context context;

  private static Integer RESULT_LOAD_VIDEO = 7;
  private static Integer RESULT_LOAD_IMG = 3;

  private RecyclerView recyclerView;
  private RecyclerView videosRecyclerView;
  private VideosRecyclerView videosAdapter;
  private ImagesRecyclerView adapter;
  private SharedPreferences sharedPreferences;
  private Activity activity;
  private ArrayList<ImageMetadata> savedImageMetadata;
  private ArrayList<VideoMetadata> savedVideoMetadata;
  private LinearLayout effectList;
  private MainApplication mainApplication;
  private ArrayList<String> effectNames = new ArrayList<String>();
  private FileUtils fileUtils;
  private TextView shop;
  private int numImages;
  private int numVideos;
  private Button uploadImageBtn;
  private Button uploadVideoBtn;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    root = inflater.inflate(R.layout.fragment_homescreen, container, false);
    activity = getActivity();
    context = activity.getApplicationContext();
    viewImages = root.findViewById(R.id.view_images);
    viewVideos = root.findViewById(R.id.view_videos);
    shop = root.findViewById(R.id.shop);
    fileUtils = new FileUtils(context);
    effectList = root.findViewById(R.id.effects);
    mainApplication = (MainApplication) activity.getApplication();
    savedImageMetadata = mainApplication.getSavedImageMetadata(context);
    savedVideoMetadata = mainApplication.getSavedVideoMetadata(context);
    sharedPreferences = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
    boolean userOnboarded = false;
    userOnboarded = sharedPreferences.getBoolean("userOnboarded", userOnboarded);

    // for testing purposes only
    onboardUser();

    if (savedImageMetadata.size() == 0) {
      // TODO display button to upload image
      initEmptyImagesButton();
    }
    if (savedVideoMetadata.size() == 0) {
      // TODO display button to upload video
      initEmptyVideosButton();
    }

    initTheme();
    setUpOnClickListeners();
    setUpImages();
    initVideos();
    initEffectList();

    mainApplication.requestPermissions(getActivity());

    return root;
  }

  private void onboardUser() {

      // TODO add tutorial with ViewPager.
      if (! sharedPreferences.contains("imageEditorTutorialCompleted")) {
          // save, as user is now onboarded
          SharedPreferences.Editor editor = sharedPreferences.edit();
          editor.putBoolean("userOnboarded", true);
          editor.apply();
      }
  }

  private void setUpImages() {
    ImageUtils imageUtils = new ImageUtils(context);
    // get num of images
    numImages = savedImageMetadata.size();
    Log.d("DEBUG", "NUM IMAGES: " + numImages);
    ArrayList<ImageMetadata> imageMetadata = null;
    if (numImages == 0) {
      imageMetadata = mainApplication.getPlaceholderMetadata();
    }
    else {
      imageMetadata = savedImageMetadata;
    }
    initRecyclerViews(imageMetadata);

  }

  private void initVideoRecyclerView() {
      videosRecyclerView = root.findViewById(R.id.saved_videos_recycler_view);
      LinearLayoutManager layoutManager
            = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

      videosRecyclerView.setLayoutManager(layoutManager);
      videosAdapter = new VideosRecyclerView(activity, savedVideoMetadata, mainApplication);

      videosRecyclerView.setAdapter(videosAdapter);

      SnapHelper helper = new PagerSnapHelper();
      helper.attachToRecyclerView(videosRecyclerView);
  }

  private void initRecyclerViews(ArrayList<ImageMetadata> imageMetadata) {
    recyclerView = root.findViewById(R.id.recycler_view_images);
    LinearLayoutManager layoutManager
            = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

    recyclerView.setLayoutManager(layoutManager);
    adapter = new ImagesRecyclerView(activity, imageMetadata, mainApplication);

    recyclerView.setAdapter(adapter);

    SnapHelper helper = new PagerSnapHelper();
    helper.attachToRecyclerView(recyclerView);
  }

  private void initTheme() {
    SharedPreferences sharedPreferences = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
    if (! sharedPreferences.contains("nightThemeEnabled")) {
      UiModeManager umm = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
      umm.setNightMode(UiModeManager.MODE_NIGHT_YES);

      // save night mode to shared preferences
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putBoolean("nightThemeEnabled", true);
      editor.apply();
    }

  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d("DEBUG", "HOME SCREEN FRAGMENT RESUMED");
    ArrayList<VideoMetadata> savedVideoMetadata = mainApplication.getSavedVideoMetadata(context);
    savedImageMetadata = mainApplication.getSavedImageMetadata(context);

    // if new images or videos detected, clear the thumbnails and re-append.
    if (savedVideoMetadata.size() != numVideos) {
      // update videos recyclerview
    }
    if (savedImageMetadata.size() != numImages) {
      adapter.updateRecyclerView(savedImageMetadata);
    }
  }

  private void initEffectList() {
      String jsonString = FileUtils.getFileFromAssets(context, MainApplication.getFilterListFilename());
      Log.d("DEBUG", jsonString);

      Gson gson = new Gson();

      List<ImageFilterMetadata> imageFilterMetadata = gson.fromJson(jsonString, new TypeToken<List<ImageFilterMetadata>>(){}.getType());
      for (int i = 0; i < imageFilterMetadata.size(); i++) {
          final String effectName = imageFilterMetadata.get(i).getName();

          TextView textView = new TextView(root.getContext());
          textView.setText(effectName);
          textView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(getActivity(), EffectDetail.class);
                  intent.putExtra("effectName", effectName);
                  startActivity(intent);
              }
          });

          effectList.addView(textView);
      }
  }

  private void setUpOnClickListeners() {
    viewImages.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getActivity(), Images.class);
        startActivity(intent);
      }
    });

    viewVideos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getActivity(), Videos.class);
        startActivity(intent);
      }
    });

    shop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), Shop.class);
            startActivity(intent);
        }
    });

  }

  private void initVideos() {
    // TODO setup RecyclerView for videos.
    initVideoRecyclerView();

  }

  private ImageView createImageView(final ImageMetadata metadata) {
    ImageView imageView = new ImageView(context);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(10, 10, 10, 10);
    imageView.setLayoutParams(layoutParams);

    File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/images");

    File file = new File(directory, metadata.getName() + ".png");

    Glide
      .with(context)
      .load(file.getAbsolutePath())
      .apply(new RequestOptions().override(150, 150))
      .into(imageView);

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, ImageEditor.class);
        ImageUtils imageUtils = new ImageUtils(getActivity().getApplicationContext());
        Image image = imageUtils.getImageFromFilename(metadata, context, mainApplication);
        mainApplication.setImage(image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          // TODO implement shared animations and transitions

        } else {

        }
        startActivity(intent);

      }
    });

    return imageView;
  }

  private ImageView createVideoThumbnail(final FileMetadata metadata) {
    ImageView imageView = new ImageView(context);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(10, 10, 10, 10);
    imageView.setLayoutParams(layoutParams);

    File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos/thumbnails");

    File file = new File(directory, metadata.getName() + ".png");

    Glide
      .with(context)
      .load(file.getAbsolutePath())
      .apply(new RequestOptions().override(150, 150))
      .into(imageView);

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, VideoEditor.class);

//        Image image = ImageUtils.getImageFromFilename(metadata, context, mainApplication);
//        mainApplication.setImage(image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          // TODO implement shared animations and transitions

        } else {

        }
        startActivity(intent);
      }
    });

    return imageView;
  }

  private void initEmptyImagesButton() {
    uploadImageBtn = root.findViewById(R.id.upload_img_home_btn);
    uploadImageBtn.setVisibility(View.VISIBLE);

    uploadImageBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        }
    });
  }

  private void initEmptyVideosButton() {
    uploadVideoBtn = root.findViewById(R.id.upload_video_home_btn);
    uploadVideoBtn.setVisibility(View.VISIBLE);
    uploadVideoBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent videoPickerIntent = new Intent(Intent.ACTION_PICK);
            videoPickerIntent.setType("video/*");
            startActivityForResult(videoPickerIntent, RESULT_LOAD_VIDEO);
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
            else if (reqCode == RESULT_LOAD_VIDEO) {
                startVideoActivity(data);
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

    private void startVideoActivity(Intent data) {
        Uri videoUri = data.getData();

        // Convert the video URI to a path
        videoUrl = fileUtils.videoUriToPath(videoUri, activity.getContentResolver());

        try {
            thumbnailVideo(videoUrl);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    class BitmapAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(Void...arg0) {
            Bitmap bitmap = null;
            FutureTarget<Bitmap> futureTarget =
                    Glide.with(context)
                            .asBitmap()
                            .load(videoUrl)
                            .submit(300, 300);

            try {
                bitmap = futureTarget.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onProgressUpdate(Integer...a) {
            super.onProgressUpdate(a);
        }

        protected void onPostExecute(Bitmap result) {
            ImageUtils imageUtils = new ImageUtils(context);
            Bitmap resizedBitmap = imageUtils.resizeBitmap(result, 200, 200);
            Intent intent = new Intent(activity.getBaseContext(), VideoEditor.class);

            if (videoUrl != null) {
                video = new Video(resizedBitmap);

                mainApplication.setVideo(video);
                intent.putExtra("videoUrl", videoUrl);
            }

            // Now that we have the bitmap thumbnail of the video, we can start the video editor activity.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.getApplication().startActivity(intent);
            super.onPostExecute(result);
        }
    }

    private void thumbnailVideo(final String videoPath) throws ExecutionException, InterruptedException {
        // thumbnail the video in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                new BitmapAsyncTask().execute();
            }
        }).start();
    }

}
