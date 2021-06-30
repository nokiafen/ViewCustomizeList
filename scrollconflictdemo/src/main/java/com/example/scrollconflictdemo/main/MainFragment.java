package com.example.scrollconflictdemo.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.example.scrollconflictdemo.Beans.MultiItem;
import com.example.scrollconflictdemo.R;
import com.example.scrollconflictdemo.adapters.MultiLayoutAdapter;

import java.util.ArrayList;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;

public class MainFragment extends Fragment {

  private MainViewModel mViewModel;
  private RecyclerView recycleview;
  private MultiLayoutAdapter adapter;

  public static MainFragment newInstance() {
    return new MainFragment();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.main_fragment, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    initSubScribe();
    // TODO: Use the ViewModel
    initView();
    mViewModel.initItem();
  }

  private void initView() {
    recycleview = getView().findViewById(R.id.recyleView);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    recycleview.setLayoutManager(layoutManager);
    recycleview.addItemDecoration(new DividerItemDecoration(getContext(), HORIZONTAL));

    //创建适配器
    adapter = new MultiLayoutAdapter(getContext(), null);
    adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    //给RecyclerView设置适配器
    recycleview.setAdapter(adapter);
  }

  private void initSubScribe() {
    mViewModel.getMutableLiveData().observe(this, new Observer<ArrayList>() {
      @Override public void onChanged(@Nullable ArrayList arrayList) {

      }
    });

    mViewModel.getMutableItemList().observe(this, new Observer<ArrayList<MultiItem>>() {
      @Override public void onChanged(@Nullable ArrayList<MultiItem> multiItems) {
        adapter.getData().addAll(multiItems);
        adapter.notifyDataSetChanged();
      }
    });
  }
}
