package com.heli.providerapp.configs;

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
public abstract  class AccessConfigBase {
  public AccessConfigBase() {
    initConfigInfo();
  }

  private void initConfigInfo() {
    getSysConfig();
    getAppConfig();
  }

  abstract public List<AccessConfigConcrect.AccessConfigBean> getSysConfig();

  abstract public  List<AccessConfigConcrect.AccessConfigBean>  getAppConfig();

  abstract public void addConfigInfo(boolean sysConfig,List<AccessConfigConcrect.AccessConfigBean> accessConfigBeanList);


}
