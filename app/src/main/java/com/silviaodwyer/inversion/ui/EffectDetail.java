package com.silviaodwyer.inversion.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.silviaodwyer.inversion.models.Image;
import com.silviaodwyer.inversion.models.ImageMetadata;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;
import com.silviaodwyer.inversion.ui.image_editor.ImageEditor;
import com.silviaodwyer.inversion.utils.ImageUtils;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class EffectDetail extends AppCompatActivity {
  private TextView effectTitle;
  private MainApplication mainApplication;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_effect_detail);
    effectTitle = findViewById(R.id.effect_detail_name);
    String effectName = getEffectName();
    effectTitle.setText(effectName);

    mainApplication = ((MainApplication)getApplication());

    // initialize file names
    initializeFileNames();
  }

  private void initializeFileNames() {
    final ImageUtils imageUtils = new ImageUtils(getApplicationContext());
    LinearLayout fileList = findViewById(R.id.effect_list);

    ArrayList<ImageMetadata> fileMetaData = mainApplication.getSavedImageMetadata(getApplicationContext());
    int length = 0;
    if (fileMetaData.size() > 3) {
      length = 3;
    }
    else {
      length = fileMetaData.size();
    }

    for (int i = 0; i < length; i++) {
      final ImageMetadata metadata = fileMetaData.get(i);
      TextView textView = new TextView(getApplicationContext());
      String fileName = metadata.getName();

      textView.setText(fileName);
      textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(EffectDetail.this, ImageEditor.class);
          Image image = imageUtils.getImageFromFilename(metadata, getApplicationContext(), mainApplication);
          mainApplication.setImage(image);
          startActivity(intent);
        }
      });
      fileList.addView(textView);
    }
  }

  private String getEffectName() throws NullPointerException {
    Bundle bundleExtras = getIntent().getExtras();
    String effectName = "";
    if(bundleExtras != null) {
      effectName = bundleExtras.getString("effectName");
    }
    else {
      throw new NullPointerException();
    }
    return effectName;
  }
}
