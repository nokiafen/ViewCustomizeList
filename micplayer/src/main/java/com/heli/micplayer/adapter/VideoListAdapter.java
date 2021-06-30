package com.heli.micplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.heli.micplayer.R;
import com.heli.micplayer.beans.VideoInfos;
import com.heli.micplayer.utils.Util;
import java.util.List;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
  private Context context;
  private List<VideoInfos> videoInfosList;
  private VideoClickLCallback videoClickLCallback;

  public VideoListAdapter(Context context, List<VideoInfos> videoInfosList) {
    this.context = context;
    this.videoInfosList = videoInfosList;
  }

  public void resetdData(List<VideoInfos> videoInfosList){
    this.videoInfosList=videoInfosList;
    notifyDataSetChanged();
  }

  public void setItemClickCallBack(VideoClickLCallback videoClickLCallback){
    this.videoClickLCallback=videoClickLCallback;
  }

  @Override
  public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(VideoListAdapter.ViewHolder holder,final int position) {
    ViewHolder vh = (ViewHolder) holder;
    vh.ivPreview.setImageBitmap(getThumb(videoInfosList.get(position).getVideoData()));
    vh.tvDescription.setText(videoInfosList.get(position).getFileName());
    vh.cardView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (videoClickLCallback!=null) {
          videoClickLCallback.onItemClick(videoInfosList.get(position));
        }
      }
    });

  }

  @Override
  public int getItemCount() {
    return videoInfosList == null ? 0 : videoInfosList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ImageView ivPreview;
    TextView tvDescription;
    CardView cardView;

    public ViewHolder(View itemView) {
      super(itemView);

      ivPreview = (ImageView) itemView.findViewById(R.id.iv_preview);
      tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
      cardView = (CardView) itemView.findViewById(R.id.card_view);
    }
  }

  private Bitmap getThumb(String path) {
    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MINI_KIND);
    return ThumbnailUtils.extractThumbnail(bitmap, Util.dp2px(context, 140),
        Util.dp2px(context, 72));
  }

  public  interface  VideoClickLCallback {
    void onItemClick(VideoInfos videoInfos);
  }
}
