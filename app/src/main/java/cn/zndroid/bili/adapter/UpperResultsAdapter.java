package cn.zndroid.bili.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.zndroid.bili.R;
import cn.zndroid.bili.adapter.helper.AbsRecyclerViewAdapter;
import cn.zndroid.bili.entity.search.SearchUpperInfo;
import cn.zndroid.bili.utils.NumberUtil;
import cn.zndroid.bili.widget.CircleImageView;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class UpperResultsAdapter extends AbsRecyclerViewAdapter {

    private List<SearchUpperInfo.DataBean.ItemsBean> uppers;


    public UpperResultsAdapter(RecyclerView recyclerView, List<SearchUpperInfo.DataBean.ItemsBean> uppers) {
        super(recyclerView);
        this.uppers = uppers;
    }
    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext()).
                inflate(R.layout.item_search_upper, parent, false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            SearchUpperInfo.DataBean.ItemsBean itemsBean = uppers.get(position);

            Glide.with(getContext())
                    .load(itemsBean.getCover())
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.mipmap.ico_user_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemViewHolder.mUserAvatar);

            itemViewHolder.mUserName.setText(itemsBean.getTitle());
            itemViewHolder.mUserFans.setText("粉丝：" + NumberUtil.converString(itemsBean.getFans()));
            itemViewHolder.mUserVideos.setText("视频数：" + NumberUtil.converString(itemsBean.getArchives()));
            itemViewHolder.mDesc.setText(itemsBean.getSign());
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return uppers.size();
    }


    public class ItemViewHolder extends ClickableViewHolder {

        CircleImageView mUserAvatar;
        TextView mUserName;
        TextView mUserFans;
        TextView mUserVideos;
        TextView mDesc;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mUserAvatar = $(R.id.item_avatar_view);
            mUserName = $(R.id.item_user_name);
            mUserFans = $(R.id.item_user_fans);
            mUserVideos = $(R.id.item_user_videos);
            mDesc = $(R.id.item_user_details);
        }
    }
}
