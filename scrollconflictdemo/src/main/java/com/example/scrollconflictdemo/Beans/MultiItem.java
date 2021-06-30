package com.example.scrollconflictdemo.Beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

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
public class MultiItem implements MultiItemEntity , Serializable {
  public static final int TYPE_VERTICAL = 1;
  public static final int TYPE_HORIZON = 2;
  private int itemType;
  private Model data;

  @Override public int getItemType() {
    return itemType;
  }

  public MultiItem(int itemType, Model data) {
    this.itemType = itemType;
    this.data = data;
  }

  public Model getData() {
    return data;
  }
}
