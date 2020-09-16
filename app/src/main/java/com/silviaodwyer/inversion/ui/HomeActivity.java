package com.silviaodwyer.inversion.ui;

import android.content.Context;
import android.os.Bundle;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.silviaodwyer.inversion.MainApplication;
import com.silviaodwyer.inversion.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    BottomNavigationView navView = findViewById(R.id.nav_view);

    NavController navigationController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupWithNavController(navView, navigationController);

    MainApplication mainApplication = ((MainApplication)getApplication());

    initTutorial();

  }

  public void initTutorial() {
      new TapTargetSequence(this)
              .targets(
                      TapTarget.forView( findViewById(R.id.navigation_effects), "Get daily edits", "Check-in here daily to get daily edits.")
                              .targetRadius(60)
                              .outerCircleColor(R.color.colorPrimary)
                              .outerCircleAlpha(0.96f)
                              .targetCircleColor(R.color.backgroundColor)
                              .titleTextSize(20)
                              .titleTextColor(R.color.textColor)
                              .dimColor(R.color.backgroundColor)
                              .cancelable(false)
                              .drawShadow(true)
                              .tintTarget(false)
                              .transparentTarget(false),
                      TapTarget.forView( findViewById(R.id.navigation_useraccount), "Customise your experience", "Set a theme, enable night mode, and more.")
                              .targetRadius(60)
                              .outerCircleColor(R.color.colorPrimaryDark)
                              .outerCircleAlpha(0.96f)
                              .targetCircleColor(R.color.backgroundColor)
                              .titleTextSize(20)
                              .titleTextColor(R.color.textColor)
                              .dimColor(R.color.backgroundColor)
                              .cancelable(false)
                              .drawShadow(true)
                              .tintTarget(false)
                              .transparentTarget(false)
                      )
              .listener(new TapTargetSequence.Listener() {
                  @Override
                  public void onSequenceFinish() {
                      // save tutorial as finished in SharedPreferences
                  }

                  @Override
                  public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                  }

                  @Override
                  public void onSequenceCanceled(TapTarget lastTarget) {
                  }
              }).start();
  }
}
