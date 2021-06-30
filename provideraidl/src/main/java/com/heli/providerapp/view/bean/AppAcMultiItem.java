package com.heli.providerapp.view.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AppAcMultiItem implements MultiItemEntity {
  public static final  int  APP_AC_LAYOUT_TYPE1=0X11;
  private  int itemType;

  public AppDetailModel getAppDetailModel() {
    return appDetailModel;
  }

  public void setAppDetailModel(AppDetailModel appDetailModel) {
    this.appDetailModel = appDetailModel;
  }

  private AppDetailModel appDetailModel;

  public AppAcMultiItem(int itemType, AppDetailModel appDetailModel) {
    this.itemType = itemType;
    this.appDetailModel = appDetailModel;
  }

  public int getItemType() {
    return itemType;
  }


}
