package com.heli.providerapp.view.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Model implements Serializable {
  private int modelType;
  private String modelTitle;

  public int getTotalAccess() {
    return access;
  }

  private int access; //1 on  0off

  public String getModelTitle() {
    return modelTitle;
  }

  public void setModelTitle(String modelTitle) {
    this.modelTitle = modelTitle;
  }

  public Model(int modelType, String modelTitle,int access) {
    this.modelType = modelType;
    this.modelTitle = modelTitle;
    this.access=access;

  }

  public  static  class  ModelBuilder {
    private int modelType;
    private String modelTitle;
    private  int access;

    public ModelBuilder buildType(int modelType){
     this.modelType=modelType;
      return  this;
    }

    public ModelBuilder buildTitle(String modelTitle){
      this.modelTitle=modelTitle;
      return  this;
    }

    public ModelBuilder buildAccess(int access){
      this.access=access;
      return  this;
    }


    public Model build(){
     return new Model(modelType,modelTitle,access);
    }


  }


}
