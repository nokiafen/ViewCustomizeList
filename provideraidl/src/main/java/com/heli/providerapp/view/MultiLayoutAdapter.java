package com.heli.providerapp.view;


import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.CompoundButton;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.heli.providerapp.AppAceessBean;
import com.heli.providerapp.NoticeAccessHelper;
import com.heli.providerapp.NoticeAccessService;
import com.heli.providerapp.R;
import com.heli.providerapp.SystemAccessBean;
import com.heli.providerapp.view.bean.AppModel;
import com.heli.providerapp.view.bean.MultiItem;
import java.util.List;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MultiLayoutAdapter extends BaseMultiItemQuickAdapter<MultiItem, BaseViewHolder> {
  private static final  String TAG="MultiLayoutAdapter";
  private Context mcontext;
  private String [] accessAppConfig;
  private PackageManager packageManager;
  public MultiLayoutAdapter(Context context,List data) {
    super(data);
    this.mcontext=context;
    addItemType(MultiItem.TYPE_SYS, R.layout.layout_adaper_sys);
    addItemType(MultiItem.TYPE_APP, R.layout.layout_adapter_app);
    accessAppConfig=context.getResources().getStringArray(R.array.app_access_config);
    packageManager=mcontext.getPackageManager();
  }



  @Override
  protected void convert(BaseViewHolder helper, final MultiItem item) {
    switch (helper.getItemViewType()) {
      case MultiItem.TYPE_SYS:
        helper.setText(R.id.sys_title, item.getData().getModelTitle());
        SystemAccessBean.SystemAccessSingle systemAccessSingle= NoticeAccessHelper.getInstance().querrySystemAccessSingleItem(mcontext,item.getData().getModelTitle());
        Log.d(TAG,"systemAccessSingle StateON: "+systemAccessSingle.isStateOn()+"");
        helper.setOnCheckedChangeListener(R.id.sys_switch,null);
        helper.setChecked(R.id.sys_switch,systemAccessSingle.isStateOn());
        helper.setOnCheckedChangeListener(R.id.sys_switch,
            new CompoundButton.OnCheckedChangeListener() {
              @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NoticeAccessHelper.getInstance().updateSystemAccess(mcontext,item.getData().getModelTitle(),isChecked?1:0);
                NoticeAccessService.notifyCommonAcChanged(item.getData().getModelTitle(),isChecked);
              }
            });
        break;
      case MultiItem.TYPE_APP:
        AppModel model= ((AppModel)item.getData());

        helper.setText(R.id.app_title, item.getData().getModelTitle());
        Log.d("GMS",item.getData().getModelTitle());
        try {
          helper.setImageDrawable(R.id.app_icon,packageManager.getApplicationIcon(model.getPackagName()));
        } catch (PackageManager.NameNotFoundException e) {
          e.printStackTrace();
        }
        AppAceessBean appAceessBean=NoticeAccessHelper.getInstance().qppQuerrySingle(mcontext,model.getPackagName(),new String[]{accessAppConfig[0]});
        helper.setText(R.id.app_state,appAceessBean.getAccessChannel().get(accessAppConfig[0])==1?"打开":"关闭");
        break;
      default:
        break;
    }
  }
}
