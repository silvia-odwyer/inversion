package com.silviaodwyer.inversion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class EffectDetail extends AppCompatActivity {
  private TextView effectTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_effect_detail);
    effectTitle = findViewById(R.id.effect_detail_name);
    String effectName = getEffectName();
    effectTitle.setText(effectName);

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
