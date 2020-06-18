package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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


    initializeFileNames();
  }

  private void initializeFileNames() {
    final ImageUtils imageUtils = new ImageUtils(getApplicationContext());
    LinearLayout fileList = findViewById(R.id.effect_list);

    ArrayList<FileMetadata> fileMetaData = mainApplication.getSavedImageMetadata(getApplicationContext());
    int length = 0;
    if (fileMetaData.size() > 3) {
      length = 3;
    }
    else {
      length = fileMetaData.size();
    }

    for (int i = 0; i < length; i++) {
      final FileMetadata metadata = fileMetaData.get(i);
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
