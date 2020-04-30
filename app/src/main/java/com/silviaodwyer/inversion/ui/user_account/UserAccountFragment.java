package com.silviaodwyer.inversion.ui.user_account;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
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

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_useraccount, container, false);

    themeToggle = root.findViewById(R.id.themeToggle);
    themeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        // TODO switch theme
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
      }
    });



    return root;
  }
}
