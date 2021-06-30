package com.heli.micplayer.mainActivity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.heli.micplayer.R;
import com.heli.micplayer.adapter.VideoListAdapter;
import com.heli.micplayer.beans.VideoInfos;
import com.heli.micplayer.utils.Util;
import java.util.ArrayList;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";
  private static final int CODE_REQUEST_STORAGE = 0x11;
  private VideoListAdapter videoListAdapter;

  @BindView(R.id.recycleView) RecyclerView recycleView;

  private MainActivityViewModel mViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
    init();
  }

  private void init() {
    initRecycleView();
    initSubscribe();
  }

  private void initRecycleView() {
    videoListAdapter=new VideoListAdapter(this,null);
    GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
    recycleView.addItemDecoration(new DividerItemDecoration(this,VERTICAL));
    recycleView.setLayoutManager(gridLayoutManager);
    recycleView.setAdapter(videoListAdapter);
    videoListAdapter.setItemClickCallBack(new VideoListAdapter.VideoClickLCallback() {
     @Override public void onItemClick(VideoInfos videoInfos) {
       //todo
       Log.d(TAG, "todo");
     }
   });
  }


  private void initSubscribe() {
    mViewModel.getFilePathList().observe(this, new Observer<ArrayList<VideoInfos>>() {
      @Override public void onChanged(@Nullable ArrayList<VideoInfos> videoInfos) {
        if (videoInfos!=null) {
          videoListAdapter.resetdData(videoInfos);
        }
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
    //mViewModel.getMedias(this);
    askForStorageAccess();
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    Log.d(TAG, "onRestoreInstanceState");
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
     EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  @AfterPermissionGranted(CODE_REQUEST_STORAGE)
  private void methodRequiresTwoPermission() {
    String[] perms = { Manifest.permission.READ_EXTERNAL_STORAGE };
    if (EasyPermissions.hasPermissions(this, perms)) {
      // Already have permission, do the thing
      // ...
      mViewModel.getMedias(this);
    } else {
      // Do not have permissions, request them now
      askForStorageAccess();
    }
  }

  private void askForStorageAccess() {
    if (!EasyPermissions.hasPermissions(this, Manifest.permission_group.STORAGE)) {
      EasyPermissions.requestPermissions(this, "rational", CODE_REQUEST_STORAGE,
          Manifest.permission.READ_EXTERNAL_STORAGE);
    }
  }

}
