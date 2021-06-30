package com.heli.providerapp;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SystemAccessBean {
  //
  private int [] access;

  public  static  class SystemAccessSingle {
    public SystemAccessSingle(boolean stateOn) {
      this.stateOn = stateOn;
    }

    private boolean stateOn;

    public boolean isStateOn() {
      return stateOn;
    }

    public void setStateOn(boolean stateOn) {
      this.stateOn = stateOn;
    }
  }
}
