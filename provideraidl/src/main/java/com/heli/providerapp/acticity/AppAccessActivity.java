package com.heli.providerapp.acticity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.heli.providerapp.AppAceessBean;
import com.heli.providerapp.NoticeAccessHelper;
import com.heli.providerapp.R;
import com.heli.providerapp.view.AppMultiLayoutAdapter;
import com.heli.providerapp.view.bean.AppAcMultiItem;
import com.heli.providerapp.view.bean.AppDetailModel;
import com.heli.providerapp.view.bean.AppModel;
import com.heli.providerapp.view.bean.MultiItem;
import java.util.ArrayList;
import java.util.List;
import xander.elasticity.ElasticityHelper;
import xander.elasticity.ORIENTATION;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.heli.providerapp.acticity.AccessTestActivity.APP_BEAN;
import static com.heli.providerapp.view.bean.AppAcMultiItem.APP_AC_LAYOUT_TYPE1;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AppAccessActivity extends AppCompatActivity {
  private RecyclerView recyclerView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.app_access_layout);
    recyclerView = findViewById(R.id.recyleView);
    init();
  }

  private void init() {
    MultiItem multiItem = (MultiItem) getIntent().getSerializableExtra(APP_BEAN);
    String[] conCurrentConfig = getResources().getStringArray(R.array.app_access_config);
    AppAceessBean appAceessBean =
        NoticeAccessHelper.getInstance()
            .qppQuerrySingle(this, ((AppModel) multiItem.getData()).getPackagName(),
                conCurrentConfig);
    List appMultiItemList = new ArrayList<AppAcMultiItem>();

    for (int i = 0; i < conCurrentConfig.length; i++) {
      appMultiItemList.add(new AppAcMultiItem(APP_AC_LAYOUT_TYPE1,
          new AppDetailModel(appAceessBean.getAccessChannel().get(conCurrentConfig[i]) == 1,
              conCurrentConfig[i],
              appAceessBean.packageName)));
    }
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new DividerItemDecoration(this,VERTICAL));
    ElasticityHelper.setUpOverScroll(recyclerView, ORIENTATION.VERTICAL);
    //创建适配器
    AppMultiLayoutAdapter adapter = new AppMultiLayoutAdapter(this, appMultiItemList);
    //给RecyclerView设置适配器
    recyclerView.setAdapter(adapter);
  }
}
