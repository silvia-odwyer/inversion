package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EffectDetail extends AppCompatActivity {
  private TextView effectTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_effect_detail);
    effectTitle = findViewById(R.id.effect_detail_name);
    String effectName = getEffectName();
    effectTitle.setText(effectName);

    initializeFileNames();
  }

  private void initializeFileNames() {
    LinearLayout fileList = findViewById(R.id.effect_list);

    // TODO Get most recently edited last 3 file items
    ArrayList<String> fileNames = new ArrayList<String>();
    fileNames.add("File1");
    fileNames.add("File2");

    for (String fileName : fileNames) {
      TextView textView = new TextView(getApplicationContext());
      textView.setText(fileName);
      textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(EffectDetail.this, ImageEditor.class);
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
