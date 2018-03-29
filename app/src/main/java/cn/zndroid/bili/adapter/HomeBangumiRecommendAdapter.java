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
import cn.zndroid.bili.entity.Bangumi.BangumiRecommendInfo;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class HomeBangumiRecommendAdapter extends AbsRecyclerViewAdapter {
    private List<BangumiRecommendInfo.ResultBean> mBangumiDetailsRecommends;

    public HomeBangumiRecommendAdapter(RecyclerView recyclerView, List<BangumiRecommendInfo.ResultBean> mBangumiDetailsRecommends) {
        super(recyclerView);
        this.mBangumiDetailsRecommends = mBangumiDetailsRecommends;
    }


    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        return new ItemViewHolder(
                LayoutInflater.from(getContext()).inflate(R.layout.item_bangumi_recommend, parent, false));
    }


    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            BangumiRecommendInfo.ResultBean resultBean = mBangumiDetailsRecommends.get(position);

            Glide.with(getContext())
                    .load(resultBean.getCover())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.bili_default_image_tv)
                    .dontAnimate()
                    .into(itemViewHolder.mImage);

            itemViewHolder.mTitle.setText(resultBean.getTitle());
            itemViewHolder.mDesc.setText(resultBean.getDesc());
            if (resultBean.getIs_new() == 1) {
                itemViewHolder.mIsNew.setVisibility(View.VISIBLE);
            } else {
                itemViewHolder.mIsNew.setVisibility(View.GONE);
            }
        }
        super.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemCount() {
        return mBangumiDetailsRecommends.size();
    }


    private class ItemViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder {

        ImageView mImage;
        TextView mTitle;
        TextView mDesc;
        ImageView mIsNew;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mImage = $(R.id.item_img);
            mTitle = $(R.id.item_title);
            mDesc = $(R.id.item_desc);
            mIsNew = $(R.id.item_is_new);
        }
    }
}
