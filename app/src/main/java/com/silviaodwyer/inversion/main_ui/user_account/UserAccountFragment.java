package com.silviaodwyer.inversion.main_ui.user_account;

import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silviaodwyer.inversion.R;

public class UserAccountFragment extends Fragment {
  private Switch themeToggle;
  private Boolean nightThemeEnabled;
  private SharedPreferences sharedPreferences;
  private Switch exploreWeeklyToggle;
  private Boolean exploreWeeklyEnabled;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_useraccount, container, false);
    sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("PREF", Context.MODE_PRIVATE);
    themeToggle = root.findViewById(R.id.themeToggle);
    exploreWeeklyToggle = root.findViewById(R.id.explore_weekly_toggle);
    initThemeToggle();
    themeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
          nightThemeEnabled = true;
          UiModeManager umm = (UiModeManager) getActivity().getApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
          umm.setNightMode(UiModeManager.MODE_NIGHT_YES);
          Toast.makeText(getActivity().getApplicationContext(), "Night theme enabled", Toast.LENGTH_SHORT).show();
        } else {
          nightThemeEnabled = false;
          UiModeManager umm = (UiModeManager) getActivity().getApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
          umm.setNightMode(UiModeManager.MODE_NIGHT_NO);
          Toast.makeText(getActivity().getApplicationContext(), "Light theme enabled", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nightThemeEnabled", nightThemeEnabled);
        editor.commit();
      }
    });

    return root;
  }

  private void initThemeToggle() {
    Boolean nightModeEnabled = false;

    // check if key exists in sharedPreferences
    if (sharedPreferences.contains("nightThemeEnabled")) {
      nightModeEnabled = sharedPreferences.getBoolean("nightThemeEnabled", nightModeEnabled);
      Log.d("DEBUG", "Night mode enabled? " + nightModeEnabled.toString());
      if (nightModeEnabled) {
        themeToggle.setChecked(true);
      }
      else {
        themeToggle.setChecked(false);
      }
    }
    else {
      themeToggle.setChecked(true);
    }
  }

  private void initExploreWeeklyToggle() {

  }
}
