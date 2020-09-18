package com.silviaodwyer.inversion.ui;

import android.content.Context;
import android.content.SharedPreferences;
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
    private SharedPreferences sharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.splash_screen_theme);
    super.onCreate(savedInstanceState);
    setTheme(R.style.AppTheme);
    setContentView(R.layout.activity_home);

    BottomNavigationView navView = findViewById(R.id.nav_view);

    NavController navigationController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupWithNavController(navView, navigationController);

    sharedPreferences = getApplicationContext().getSharedPreferences("PREF", Context.MODE_PRIVATE);

    initTutorial();

  }

  public void initTutorial() {
      if (! sharedPreferences.contains("homeTutorialCompleted")) {
          new TapTargetSequence(this)
                  .targets(
                          TapTarget.forView(findViewById(R.id.navigation_effects), "Get daily edits", "Check-in here to get daily edits and remixes of your photos.")
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
                          TapTarget.forView(findViewById(R.id.navigation_useraccount), "Customise your experience", "Set a theme, enable night mode, and more.")
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
                          SharedPreferences.Editor editor = sharedPreferences.edit();
                          editor.putBoolean("homeTutorialCompleted", true);
                          editor.apply();
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
}
