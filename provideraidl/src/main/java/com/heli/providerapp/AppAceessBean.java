package com.heli.providerapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import java.util.Map;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AppAceessBean {
  public String packageName;
  public int id;
  public int icon;
  public String appName;

  private Map<String, Integer> accessChannel;

  public AppAceessBean(int id, String packageName, Map<String, Integer> accessChannel, int icon,
      String appName) {
    this.id = id;
    this.packageName = packageName;
    this.accessChannel = accessChannel;
    this.icon = icon;
    this.appName = appName;
  }

  public void setAccessChannel(Map<String, Integer> accessChannel) {
    this.accessChannel = accessChannel;
  }

  public Map<String, Integer> getAccessChannel() {
    return accessChannel;
  }

  public static class Builder {
    private int id;
    private String packageName;
    private int icon;
    private String appName;

    private Map<String, Integer> accessChannel;

    public Builder buildId(int id) {
      this.id = id;
      return this;
    }

    public Builder buildPackageName(String packageName) {
      this.packageName = packageName;
      return this;
    }

    public Builder buildIcon(int icon) {
      this.icon = icon;
      return this;
    }

    public Builder buildAppName(String appName ){
      this.appName = appName;
      return this;
    }

    public Builder buildAccessMap(Map<String, Integer> accessChannel) {
      this.accessChannel = accessChannel;
      return this;
    }

    public AppAceessBean build() {
      return new AppAceessBean(id, packageName, accessChannel, icon, appName);
    }
  }
}
