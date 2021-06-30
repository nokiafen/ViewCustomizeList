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
public class AppModel extends Model implements Serializable {
    public String getPackagName() {
        return packagName;
    }

    private String packagName;
    private int modelType;
    private String modelTitle;

    public int getTotalAccess() {
        return totalAccess;
    }

    private int totalAccess; //1 on  0off

    private int iconId;

    private String appName;

    public int getIconId() {
        return iconId;
    }

    public String getAppName() {
        return appName;
    }

    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public AppModel(int modelType, String modelTitle, int totalAccess, String packagName,int iconId,String appName) {
        super(modelType, modelTitle, totalAccess);
        this.modelType = modelType;
        this.modelTitle = modelTitle;
        this.totalAccess = totalAccess;
        this.packagName = packagName;
        this.iconId=iconId;
        this.appName=appName;

    }

    public static class ModelBuilder {
        private int modelType;
        private String modelTitle;
        private int access;
        private String packagName;
        private int iconId;
        private String appName;

        public ModelBuilder buildType(int modelType) {
            this.modelType = modelType;
            return this;
        }

        public ModelBuilder buildTitle(String modelTitle) {
            this.modelTitle = modelTitle;
            return this;
        }

        public ModelBuilder buildAccess(int access) {
            this.access = access;
            return this;
        }

        public ModelBuilder buildPackagaeName(String packagName) {
            this.packagName = packagName;
            return this;
        }

        public ModelBuilder buildAppName(String appName) {
            this.appName = packagName;
            return this;
        }
        public ModelBuilder buildId(int iconId) {
            this.iconId = iconId;
            return this;
        }

        public AppModel build() {
            return new AppModel(modelType, modelTitle, access, packagName,iconId,appName);
        }

    }


}
