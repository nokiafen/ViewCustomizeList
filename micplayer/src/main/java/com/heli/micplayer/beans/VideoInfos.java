package com.heli.micplayer.beans;

public class VideoInfos {
  private  String videoData;
  private String  fileName;

  public String getVideoData() {
    return videoData;
  }

  public void setVideoData(String videoData) {
    this.videoData = videoData;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  private VideoInfos(Builder builder) {
    videoData = builder.videoData;
    fileName = builder.fileName;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String videoData;
    private String fileName;

    private Builder() {
    }

    public Builder videoData(String val) {
      videoData = val;
      return this;
    }

    public Builder fileName(String val) {
      fileName = val;
      return this;
    }

    public VideoInfos build() {
      return new VideoInfos(this);
    }
  }
}
