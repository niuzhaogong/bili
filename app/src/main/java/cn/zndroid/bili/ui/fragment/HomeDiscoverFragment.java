package cn.zndroid.bili.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.zndroid.bili.R;
import cn.zndroid.bili.entity.discover.HotSearchTag;
import cn.zndroid.bili.net.RetrofitHelper;
import cn.zndroid.bili.ui.activitys.ActivityCenterActivity;
import cn.zndroid.bili.ui.activitys.AllAreasRankActivity;
import cn.zndroid.bili.ui.activitys.BrowserActivity;
import cn.zndroid.bili.ui.activitys.GameCentreActivity;
import cn.zndroid.bili.ui.activitys.OriginalRankActivity;
import cn.zndroid.bili.ui.activitys.TopicCenterActivity;
import cn.zndroid.bili.ui.activitys.TotalStationSearchActivity;
import cn.zndroid.bili.utils.ConstantUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class HomeDiscoverFragment extends RxLazyFragment {

    @BindView(R.id.tags_layout)
    TagFlowLayout mTagFlowLayout;
    @BindView(R.id.hide_scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.hide_tags_layout)
    TagFlowLayout mHideTagLayout;
    @BindView(R.id.more_layout)
    LinearLayout mMoreLayout;
    @BindView(R.id.tv_more)
    TextView mMoreText;

    private boolean isShowMore = true;
    private List<HotSearchTag.ListBean> hotSearchTags = new ArrayList<>();

    public static HomeDiscoverFragment newInstance() {
        return new HomeDiscoverFragment();
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_discover;
    }


    @Override
    public void finishCreateView(Bundle state) {
        mScrollView.setNestedScrollingEnabled(true);
        getTags();
    }


    private void getTags() {
        RetrofitHelper.getSearchAPI()
                .getHotSearchTags()
                .compose(bindToLifecycle())
                .map(HotSearchTag::getList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listBeans -> {
                    hotSearchTags.addAll(listBeans);
                    initTagLayout();
                }, throwable -> {

                });
    }


    private void initTagLayout() {
        //获取热搜标签集合前9个默认显示
        List<HotSearchTag.ListBean> frontTags = hotSearchTags.subList(0, 8);
        mTagFlowLayout.setAdapter(new TagAdapter<HotSearchTag.ListBean>(frontTags) {
            @Override
            public View getView(FlowLayout parent, int position, HotSearchTag.ListBean listBean) {
                TextView mTags = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_tags_item, parent, false);
                mTags.setText(listBean.getKeyword());
                mTags.setOnClickListener(v -> TotalStationSearchActivity.launch(getActivity(), listBean.getKeyword()));
                return mTags;
            }
        });
        mHideTagLayout.setAdapter(new TagAdapter<HotSearchTag.ListBean>(hotSearchTags) {
            @Override
            public View getView(FlowLayout parent, int position, HotSearchTag.ListBean listBean) {
                TextView mTags = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_tags_item, parent, false);
                mTags.setText(listBean.getKeyword());
                mTags.setOnClickListener(v -> TotalStationSearchActivity.launch(getActivity(), listBean.getKeyword()));
                return mTags;
            }
        });
    }


    @OnClick(R.id.more_layout)
    void showAndHideMoreLayout() {
        if (isShowMore) {
            isShowMore = false;
            mScrollView.setVisibility(View.VISIBLE);
            mMoreText.setText("收起");
            mTagFlowLayout.setVisibility(View.GONE);
            Drawable upDrawable = getResources().getDrawable(R.mipmap.ic_arrow_up_gray_round);
            upDrawable.setBounds(0, 0, upDrawable.getMinimumWidth(), upDrawable.getMinimumHeight());
            mMoreText.setCompoundDrawables(upDrawable, null, null, null);
        } else {
            isShowMore = true;
            mScrollView.setVisibility(View.GONE);
            mMoreText.setText("查看更多");
            mTagFlowLayout.setVisibility(View.VISIBLE);
            Drawable downDrawable = getResources().getDrawable(R.mipmap.ic_arrow_down_gray_round);
            downDrawable.setBounds(0, 0, downDrawable.getMinimumWidth(), downDrawable.getMinimumHeight());
            mMoreText.setCompoundDrawables(downDrawable, null, null, null);
        }
    }


    /**
     * 前往话题中心界面
     */
    @OnClick(R.id.topic_center_layout)
    void startTopicCenterActivity() {
        startActivity(new Intent(getActivity(), TopicCenterActivity.class));
    }


    /**
     * 前往活动中心界面
     */
    @OnClick(R.id.activity_center_layout)
    void startActivityCenterActivity() {
        startActivity(new Intent(getActivity(), ActivityCenterActivity.class));
    }


    /**
     * 前往全区排行榜界面
     */
    @OnClick(R.id.layout_all_rank)
    void startAllRankActivity() {
        startActivity(new Intent(getActivity(), AllAreasRankActivity.class));
    }


    /**
     * 前往原创排行榜界面
     */
    @OnClick(R.id.layout_original)
    void startOriginalRankActivity() {
        startActivity(new Intent(getActivity(), OriginalRankActivity.class));
    }


    /**
     * 前往游戏中心界面
     */
    @OnClick(R.id.layout_game_center)
    void startGameCenterActivity() {
        startActivity(new Intent(getActivity(), GameCentreActivity.class));
    }


    /**
     * 前往搜索界面
     */
    @OnClick(R.id.card_view)
    void startSearchActivity() {
        startActivity(new Intent(getActivity(), TotalStationSearchActivity.class));
    }


    @OnClick(R.id.layout_shop)
    void startShop() {
        BrowserActivity.launch(getActivity(), ConstantUtil.SHOP_URL, "bilibili - 周边商城");
    }
}
