package com.heli.micplayer.ui.homeactivity2;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.heli.micplayer.R;

public class HomeActivity2Fragment extends Fragment {

  private HomeActivity2ViewModel mViewModel;

  public static HomeActivity2Fragment newInstance() {
    return new HomeActivity2Fragment();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.home_activity2_fragment, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(HomeActivity2ViewModel.class);
    // TODO: Use the ViewModel
  }
}
