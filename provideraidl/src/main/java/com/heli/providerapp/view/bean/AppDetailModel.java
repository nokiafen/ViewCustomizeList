package com.heli.providerapp.view.bean;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AppDetailModel {
  private boolean accessStateOn;
  private String title;

  public String getPakgName() {
    return pakgName;
  }

  public void setPakgName(String pakgName) {
    this.pakgName = pakgName;
  }

  private String pakgName;

  public boolean isAccessStateOn() {
    return accessStateOn;
  }

  public void setAccessStateOn(boolean accessStateOn) {
    this.accessStateOn = accessStateOn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public AppDetailModel(boolean accessStateOn, String title, String pakgName) {
    this.accessStateOn = accessStateOn;
    this.title = title;
    this.pakgName=pakgName;
  }
}
