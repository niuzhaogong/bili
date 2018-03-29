package cn.zndroid.bili.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.zndroid.bili.R;
import cn.zndroid.bili.adapter.helper.AbsRecyclerViewAdapter;
import cn.zndroid.bili.entity.video.VideoDetailsInfo;
import cn.zndroid.bili.utils.NumberUtil;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class VideoRelatedAdapter extends AbsRecyclerViewAdapter{
    private List<VideoDetailsInfo.DataBean.RelatesBean> relates;

    public VideoRelatedAdapter(RecyclerView recyclerView, List<VideoDetailsInfo.DataBean.RelatesBean> relates) {
        super(recyclerView);
        this.relates = relates;
    }


    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext()).
                inflate(R.layout.item_video_strip, parent, false));
    }


    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            VideoDetailsInfo.DataBean.RelatesBean relatesBean = relates.get(position);

            Glide.with(getContext())
                    .load(relatesBean.getPic())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.bili_default_image_tv)
                    .dontAnimate()
                    .into(itemViewHolder.mVideoPic);

            itemViewHolder.mVideoTitle.setText(relatesBean.getTitle());
            itemViewHolder.mVideoPlayNum.setText(
                    NumberUtil.converString(relatesBean.getStat().getView()));
            itemViewHolder.mVideoReviewNum.setText(
                    NumberUtil.converString(relatesBean.getStat().getDanmaku()));
            itemViewHolder.mUpName.setText(relatesBean.getOwner().getName());
        }
        super.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemCount() {
        return relates.size();
    }


    public class ItemViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder {

        ImageView mVideoPic;
        TextView mVideoTitle;
        TextView mVideoPlayNum;
        TextView mVideoReviewNum;
        TextView mUpName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mVideoPic = $(R.id.item_img);
            mVideoTitle = $(R.id.item_title);
            mVideoPlayNum = $(R.id.item_play);
            mVideoReviewNum = $(R.id.item_review);
            mUpName = $(R.id.item_user_name);
        }
    }
}
