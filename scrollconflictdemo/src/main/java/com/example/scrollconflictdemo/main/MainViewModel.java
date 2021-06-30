package com.example.scrollconflictdemo.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;


import com.example.scrollconflictdemo.Beans.Model;
import com.example.scrollconflictdemo.Beans.MultiItem;

import java.util.ArrayList;
import java.util.Random;

import static com.example.scrollconflictdemo.Beans.MultiItem.TYPE_VERTICAL;


public class MainViewModel extends ViewModel {
  // TODO: Implement the ViewModel
  private MutableLiveData<ArrayList> mutableLiveData = new MutableLiveData<>();

  public MutableLiveData<ArrayList<MultiItem>> getMutableItemList() {
    return mutableItemList;
  }

  private MutableLiveData<ArrayList<MultiItem>> mutableItemList =
      new MutableLiveData<ArrayList<MultiItem>>();

  public MutableLiveData<ArrayList> getMutableLiveData() {
    return mutableLiveData;
  }

  @Override protected void onCleared() {
    super.onCleared();
  }

  public void initItem() {
    ArrayList<MultiItem> itemLists = mutableItemList.getValue();
    if (itemLists==null) {
      itemLists=new ArrayList<>();
    }
    itemLists.clear();
    Random random = new Random();
    for (int i = 0; i < 80; i++) {
      itemLists.add(new MultiItem(TYPE_VERTICAL, new Model(random.nextInt(100))));
    }
    mutableItemList.setValue(itemLists);
  }
}
