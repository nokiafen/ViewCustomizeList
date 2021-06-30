package com.heli.micplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.heli.micplayer.ui.homeactivity2.HomeActivity2Fragment;

public class HomeActivity2 extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home_activity2_activity);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.container, HomeActivity2Fragment.newInstance())
          .commitNow();
    }
  }
}
