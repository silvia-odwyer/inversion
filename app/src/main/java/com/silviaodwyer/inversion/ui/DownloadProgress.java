package com.silviaodwyer.inversion.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.gpuv.composer.FillMode;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.egl.filter.GlSepiaFilter;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.models.Video;
import com.silviaodwyer.inversion.models.VideoMetadata;
import com.silviaodwyer.inversion.utils.ImageUtils;
import com.silviaodwyer.inversion.video_filters.VideoFilters;

import java.io.File;

public class DownloadProgress extends AppCompatActivity {
    private Video video;
    private ProgressBar progressBar;
    private String videoPath;
    private GlFilter activeFilter = new GlSepiaFilter();
    private MainApplication mainApplication;
    private String videoFilterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_progress);
        mainApplication = ((MainApplication)getApplication());
        video = mainApplication.getVideo();
        VideoFilters vf = new VideoFilters(getApplicationContext());

        // get the video path
        videoPath = getIntent().getExtras().getString("videoPath");
        videoFilterName = getIntent().getExtras().getString("videoFilterName");
        Log.d("DEBUG", "VIDEO FILTER NAME: " + videoFilterName);
        activeFilter = vf.getVideoFilterFromName(videoFilterName);

        this.initVideoDetailCard();
        this.saveVideo();
    }

    private void saveVideo() {
        initProgressBar();

        ImageUtils imageUtils = new ImageUtils(getApplicationContext());
        File dst = new File(Environment.getExternalStorageDirectory().toString() + "/Inversion/videos");
        dst.mkdirs();
        VideoMetadata metadata = new VideoMetadata(videoPath);
        video.setMetadata(metadata);

        File outputFile = new File(dst.getPath() + File.separator + video.getMetadata().getName() + ".mp4");
        final String destMp4Path = outputFile.getPath();

        new GPUMp4Composer(videoPath, destMp4Path)
                .fillMode(FillMode.PRESERVE_ASPECT_CROP)
                .filter(activeFilter)
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        int progress_res = (int) Math.round((progress * 100));
                        progressBar.setProgress(progress_res);
                    }

                    @Override
                    public void onCompleted() {
                        progressBar.setProgress(100);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));
                        getContentResolver().notifyChange(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);

                        // Save video to store
                        mainApplication.saveVideoMetadata(video.getMetadata());
                        imageUtils.writeThumbnail(video);
                    }

                    @Override
                    public void onCanceled() {
                        Log.d("DEBUG", "Saving video cancelled");
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.e("DEBUG", "onFailed()", exception);
                        Toast.makeText(getApplicationContext(), "Error when saving video.", Toast.LENGTH_SHORT).show();

                    }
                })
                .start();
    }

    private void initProgressBar() {
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initVideoDetailCard() {
        ImageView videoThumbnail = findViewById(R.id.vid_saved_thumbnail);
        videoThumbnail.setImageBitmap(mainApplication.getVideo().getThumbnail());

        TextView videoName = findViewById(R.id.vid_name);
        videoName.setText(video.getMetadata().getName());
    }
}
