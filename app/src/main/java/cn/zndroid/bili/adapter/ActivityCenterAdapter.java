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
import cn.zndroid.bili.entity.discover.ActivityCenterInfo;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class ActivityCenterAdapter extends AbsRecyclerViewAdapter {
    private List<ActivityCenterInfo.ListBean> activityCenters;

    public ActivityCenterAdapter(RecyclerView recyclerView, List<ActivityCenterInfo.ListBean> activityCenters) {
        super(recyclerView);
        this.activityCenters = activityCenters;
    }


    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext())
                .inflate(R.layout.item_activity_center_list, parent, false));
    }


    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            ActivityCenterInfo.ListBean listBean = activityCenters.get(position);

            Glide.with(getContext())
                    .load(listBean.getCover())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.bili_default_image_tv)
                    .dontAnimate()
                    .into(itemViewHolder.mImage);

            itemViewHolder.mTitle.setText(listBean.getTitle());

            if (listBean.getState() == 1) {
                itemViewHolder.mState.setImageResource(R.mipmap.ic_badge_end);
            } else {
                itemViewHolder.mState.setImageResource(R.mipmap.ic_badge_going);
            }
        }
        super.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemCount() {
        return activityCenters.size();
    }


    private class ItemViewHolder extends ClickableViewHolder {

        ImageView mImage;
        TextView mTitle;
        ImageView mState;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mImage = $(R.id.item_image);
            mTitle = $(R.id.item_title);
            mState = $(R.id.item_state);
        }
    }
}
