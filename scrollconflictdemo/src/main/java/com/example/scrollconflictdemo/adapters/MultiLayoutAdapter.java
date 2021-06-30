package com.example.scrollconflictdemo.adapters;


import android.content.Context;
import android.content.pm.PackageManager;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.scrollconflictdemo.Beans.MultiItem;
import com.example.scrollconflictdemo.R;
import com.example.scrollconflictdemo.Views.VerticalBar;

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
    addItemType(MultiItem.TYPE_VERTICAL, R.layout.layout_adaper_sys);
    //addItemType(MultiItem.TYPE_HORIZON, R.layout.layout_adapter_app);
    packageManager=mcontext.getPackageManager();
  }



  @Override
  protected void convert(BaseViewHolder helper, final MultiItem item) {
    switch (helper.getItemViewType()) {
      case MultiItem.TYPE_VERTICAL:
        VerticalBar bar=helper.getView(R.id.verticalBar);
        bar.setProgress(item.getData().getProgress());
//        bar.startAim(item.getData().getProgress());
        break;
      case MultiItem.TYPE_HORIZON:

        break;
      default:
        break;
    }
  }

  @Override public int getItemCount() {
    return super.getItemCount();
  }
}
