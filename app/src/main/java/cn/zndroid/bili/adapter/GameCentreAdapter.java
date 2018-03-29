package cn.zndroid.bili.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.zndroid.bili.R;
import cn.zndroid.bili.adapter.helper.AbsRecyclerViewAdapter;
import cn.zndroid.bili.entity.discover.GameCenterInfo;
import cn.zndroid.bili.ui.activitys.BrowserActivity;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class GameCentreAdapter extends AbsRecyclerViewAdapter {

    private List<GameCenterInfo.ItemsBean> items;

    public GameCentreAdapter(RecyclerView recyclerView, List<GameCenterInfo.ItemsBean> items) {
        super(recyclerView);
        this.items = items;
    }


    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext()).
                inflate(R.layout.item_game_center, parent, false));
    }


    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder mHolder = (ItemViewHolder) holder;
            GameCenterInfo.ItemsBean itemsBean = items.get(position);

            Glide.with(getContext())
                    .load(itemsBean.getCover())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.bili_default_image_tv)
                    .dontAnimate()
                    .into(mHolder.mImageView);

            mHolder.mTitle.setText(itemsBean.getTitle());
            mHolder.mDesc.setText(itemsBean.getSummary());
            mHolder.mButton.setOnClickListener(v -> BrowserActivity.
                    launch((Activity) getContext(),
                            itemsBean.getDownload_link(), itemsBean.getTitle()));
        }
        super.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ItemViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder {

        ImageView mImageView;
        TextView mTitle;
        TextView mDesc;
        Button mButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mImageView = $(R.id.item_img);
            mTitle = $(R.id.item_title);
            mDesc = $(R.id.item_desc);
            mButton = $(R.id.item_btn);
        }
    }
}
