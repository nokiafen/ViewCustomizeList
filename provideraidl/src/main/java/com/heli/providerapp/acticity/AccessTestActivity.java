package com.heli.providerapp.acticity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.heli.providerapp.AppAceessBean;
import com.heli.providerapp.NoticeAccessHelper;
import com.heli.providerapp.NoticeAccessService;
import com.heli.providerapp.R;
import com.heli.providerapp.view.MultiLayoutAdapter;
import com.heli.providerapp.view.bean.AppModel;
import com.heli.providerapp.view.bean.Model;
import com.heli.providerapp.view.bean.MultiItem;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import xander.elasticity.ElasticityHelper;
import xander.elasticity.ORIENTATION;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.heli.providerapp.view.bean.MultiItem.TYPE_APP;
import static com.heli.providerapp.view.bean.MultiItem.TYPE_SYS;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AccessTestActivity extends AppCompatActivity {
  public final  static  String APP_BEAN="app_bean_";
  private RecyclerView recyclerView;
  private Handler handler;
  private MultiLayoutAdapter adapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.access_layout);
    recyclerView = findViewById(R.id.recyleView);
    handler = new Handler();
    handler.postDelayed(runnable,500);
    findViewById(R.id.bt_reset).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        NoticeAccessService.resetAccesses(AccessTestActivity.this);
        adapter.notifyDataSetChanged();
      }
    });
  }

  private Runnable runnable = new Runnable() {
    @Override public void run() {
      if (NoticeAccessService.isInit) {
        init();
      } else {
        handler.postDelayed(this,3000);
      }
      //DBUtils.getInstance().checkTableCul(NoticeAccessSQLOpenHelper.TABLE_NAME_SYS_ACCESS,"AccessTitle","varchar(256)");
    }
  };

  private void init() {
    List<MultiItem> modelList = new ArrayList<>();
    String [] accessConfig= getResources().getStringArray(R.array.system_access_config);
    Map<String,Integer> sysAccess = NoticeAccessHelper.getInstance().querryAllSystemAccess(this);
    if (sysAccess != null) {
      for(String key :accessConfig){
          modelList.add(new MultiItem(TYPE_SYS, new Model.ModelBuilder().buildTitle(key).buildAccess(sysAccess.get(key)).buildType(0).build()));
      }
    }
    /*******************************  appInfo below **************************************/
    List<AppAceessBean> appAceessBeanList = NoticeAccessHelper.getInstance().appQuerryAll(this, getResources().getStringArray(R.array.app_access_config));
    for (AppAceessBean appAceessBean : appAceessBeanList) {
      modelList.add(new MultiItem(TYPE_APP, new AppModel.ModelBuilder().buildTitle(appAceessBean.appName).
          buildPackagaeName(appAceessBean.packageName).buildType(1).buildAppName(appAceessBean.appName)
          .buildId(appAceessBean.icon).build()));
    }
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new DividerItemDecoration(this,VERTICAL));
    ElasticityHelper.setUpOverScroll(recyclerView, ORIENTATION.VERTICAL);
    //new GravitySnapHelper(Gravity.END).attachToRecyclerView(recyclerView);
    //创建适配器
    adapter = new MultiLayoutAdapter(this,modelList);
    adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    //给RecyclerView设置适配器
    recyclerView.setAdapter(adapter);
    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
       MultiItem multiItem= (MultiItem) adapter.getData().get(position);
        if (multiItem.getItemType()== MultiItem.TYPE_APP) {
          Intent intent =new Intent(AccessTestActivity.this,AppAccessActivity.class);
          intent.putExtra(APP_BEAN,multiItem);
          startActivity(intent);
        }
      }
    });
  }
}
