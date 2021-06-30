package com.heli.providerapp.configs;

import com.heli.providerapp.R;
import com.heli.providerapp.acticity.MyApplication;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/08/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AccessConfigConcrect extends AccessConfigBase {
  private static AccessConfigConcrect accessConfigConcrect;
  private List<AccessConfigBean> sysConfigBeanList;
  private List<AccessConfigBean> appConfigBeanList;

  public List<AccessConfigBean> getSysConfigBeanList() {
    return sysConfigBeanList;
  }

  public void setSysConfigBeanList(
      List<AccessConfigBean> sysConfigBeanList) {
    this.sysConfigBeanList = sysConfigBeanList;
  }

  public List<AccessConfigBean> getAppConfigBeanList() {
    return appConfigBeanList;
  }

  public void setAppConfigBeanList(
      List<AccessConfigBean> appConfigBeanList) {
    this.appConfigBeanList = appConfigBeanList;
  }

  public AccessConfigConcrect() {
    super();
  }

  public static AccessConfigConcrect getInstance() {
    if (accessConfigConcrect == null) {
      accessConfigConcrect = new AccessConfigConcrect();
    }
    return accessConfigConcrect;
  }

  @Override public List<AccessConfigBean> getSysConfig() {
    if (sysConfigBeanList == null) {
      sysConfigBeanList = new ArrayList<>();
    }
    sysConfigBeanList.clear();;
    addConfigInfo(true,sysConfigBeanList);
    return sysConfigBeanList;
  }

  @Override public List<AccessConfigBean> getAppConfig() {
    if (appConfigBeanList == null) {
      appConfigBeanList = new ArrayList<>();
    }
    appConfigBeanList.clear();;
    addConfigInfo(false,appConfigBeanList);
    return appConfigBeanList;
  }

  @Override
  public void addConfigInfo(boolean sysConfig, List<AccessConfigBean> accessConfigBeanList) {
    if (sysConfig) {
      int[] sysAccessArray=MyApplication.getContext().getResources().getIntArray(R.array.system_access_config);
      for (int i = 0; i < sysAccessArray.length; i++) {
        accessConfigBeanList.add(new AccessConfigBean(0,"系统通知"+i,i%2==0));
      }
    }else {
      int[] appAccessArray=MyApplication.getContext().getResources().getIntArray(R.array.app_access_config);
      for (int i = 0; i < appAccessArray.length; i++) {
        accessConfigBeanList.add(new AccessConfigBean(0,"应用接收通知"+i,i%2==0));
      }
    }

  }

  public static class AccessConfigBean {
    private int functionIndex;
    private String title;
    private boolean functionOpen;

    public AccessConfigBean(int functionIndex, String title, boolean functionOpen) {
      this.functionIndex = functionIndex;
      this.title = title;
      this.functionOpen = functionOpen;
    }

    public int getFunctionIndex() {
      return functionIndex;
    }

    public void setFunctionIndex(int functionIndex) {
      this.functionIndex = functionIndex;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public boolean isFunctionOpen() {
      return functionOpen;
    }

    public void setFunctionOpen(boolean functionOpen) {
      this.functionOpen = functionOpen;
    }
  }
}
