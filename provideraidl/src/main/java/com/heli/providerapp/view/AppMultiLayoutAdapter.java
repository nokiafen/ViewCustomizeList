package com.heli.providerapp.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.heli.providerapp.AppAceessBean;
import com.heli.providerapp.NoticeAccessHelper;
import com.heli.providerapp.NoticeAccessService;
import com.heli.providerapp.R;
import com.heli.providerapp.view.bean.AppAcMultiItem;
import java.util.List;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AppMultiLayoutAdapter
    extends BaseMultiItemQuickAdapter<AppAcMultiItem, BaseViewHolder> {
  private static final String TAG = "AppMultiLayoutAdapter";
  private Context mcontext;
  private boolean subViewFolding=false;


  public AppMultiLayoutAdapter(Context context, List data) {
    super(data);
    this.mcontext = context;
    addItemType(AppAcMultiItem.APP_AC_LAYOUT_TYPE1, R.layout.layout_adaper_sys);
  }

  @Override
  protected void convert(BaseViewHolder helper, final AppAcMultiItem item) {
    switch (helper.getItemViewType()) {
      case AppAcMultiItem.APP_AC_LAYOUT_TYPE1:
        helper.setText(R.id.sys_title, item.getAppDetailModel().getTitle());
        final AppAceessBean appAceessBean = NoticeAccessHelper.getInstance()
            .qppQuerrySingle(mcontext, item.getAppDetailModel().getPakgName(),
                mcontext.getResources().getStringArray(R.array.app_access_config));
        boolean stateOn = false;
        try {
          stateOn = appAceessBean.getAccessChannel().get(item.getAppDetailModel().getTitle()) == 1;
        } catch (Exception e) {
          e.printStackTrace();
        }
        Log.d(TAG, "appAccessSingle StateON: " + stateOn);
        helper.setOnCheckedChangeListener(R.id.sys_switch, null);
        helper.setChecked(R.id.sys_switch,
            stateOn);
        helper.setOnCheckedChangeListener(R.id.sys_switch,
            new CompoundButton.OnCheckedChangeListener() {
              @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NoticeAccessHelper.getInstance()
                    .appInfoUpdate(mcontext, item.getAppDetailModel().getPakgName(),
                        new String[] { item.getAppDetailModel().getTitle() },
                        new int[] { isChecked ? 1 : 0 });
                NoticeAccessService.notifyAPPAcChanged(item.getAppDetailModel().getPakgName(),item.getAppDetailModel().getTitle(),isChecked);
                if (getData().indexOf(item)==0) {
                  subViewFolding=!isChecked;
                  notifyDataSetChanged();
                }
              }
            });

        break;

      default:
        break;
    }
  }

  @Override public int getItemCount() {
      return  subViewFolding?1:getData().size();
  }
}
