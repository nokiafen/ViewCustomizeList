package com.heli.micplayer.mainActivity;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.heli.micplayer.beans.VideoInfos;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivityViewModel extends ViewModel {

  public MutableLiveData<ArrayList<VideoInfos>> getFilePathList() {
    return filePathList;
  }

  private MutableLiveData<ArrayList<VideoInfos>> filePathList = new MutableLiveData<>();

  public void getMedias(Context context) {
    ArrayList<VideoInfos> downloadedList = new ArrayList<>();
    String[] projection =
        { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME };
          Cursor cursor = context.getContentResolver()
        .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
    VideoInfos videoInfos;
    try {
      cursor.moveToFirst();
      do {
        String data = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
        String displayName = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
        //String title = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
        videoInfos = VideoInfos.newBuilder().videoData(data).fileName(displayName).build();
        downloadedList.add(videoInfos);
      } while (cursor.moveToNext());
      cursor.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    filePathList.postValue(downloadedList);
  }
}
