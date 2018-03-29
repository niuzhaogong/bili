package cn.zndroid.bili.adapter;

import android.annotation.SuppressLint;
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
import cn.zndroid.bili.entity.Bangumi.BangumiDetailsRecommendInfo;
import cn.zndroid.bili.utils.NumberUtil;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class BangumiDetailsRecommendAdapter extends AbsRecyclerViewAdapter{
    private List<BangumiDetailsRecommendInfo.ResultBean.ListBean> bangumiRecommends;

    public BangumiDetailsRecommendAdapter(RecyclerView recyclerView, List<BangumiDetailsRecommendInfo.ResultBean.ListBean> bangumiRecommends) {
        super(recyclerView);
        this.bangumiRecommends = bangumiRecommends;
    }


    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext())
                .inflate(R.layout.item_bangumi_details_recommend, parent, false));
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            BangumiDetailsRecommendInfo.ResultBean.ListBean listBean = bangumiRecommends.get(position);

            Glide.with(getContext())
                    .load(listBean.getCover())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.bili_default_image_tv)
                    .dontAnimate()
                    .into(itemViewHolder.mImage);

            itemViewHolder.mTitle.setText(listBean.getTitle());
            itemViewHolder.mFollow.setText(
                    NumberUtil.converString(Integer.valueOf(listBean.getFollow())) + "人追番");
        }
        super.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemCount() {

        return bangumiRecommends.size();
    }


    private class ItemViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder {

        ImageView mImage;

        TextView mTitle;

        TextView mFollow;


        public ItemViewHolder(View itemView) {

            super(itemView);
            mImage = $(R.id.item_img);
            mTitle = $(R.id.item_title);
            mFollow = $(R.id.item_follow);
        }
    }
}
