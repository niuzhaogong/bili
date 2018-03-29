package cn.zndroid.bili.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.zndroid.bili.R;
import cn.zndroid.bili.adapter.AttentionDynamicAdapter;
import cn.zndroid.bili.entity.attention.AttentionDynamicInfo;
import cn.zndroid.bili.net.RetrofitHelper;
import cn.zndroid.bili.utils.SnackbarUtil;
import cn.zndroid.bili.widget.CustomEmptyView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class HomeAttentionFragment extends RxLazyFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.layout_top)
    RelativeLayout topLayout;

    private boolean mIsRefreshing = false;
    private List<AttentionDynamicInfo.DataBean.FeedsBean> dynamics = new ArrayList<>();

    public static HomeAttentionFragment newInstance() {
        return new HomeAttentionFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_attention;
    }

    @Override
    public void finishCreateView(Bundle state) {
        isPrepared = true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initRefreshLayout();
        isPrepared = false;
    }

    @Override
    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            loadData();
        });
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            clearData();
            loadData();
        });
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getBiliAPI()
                .getAttentionDynamic()
                .compose(bindToLifecycle())
                .map(attentionDynamicInfo -> attentionDynamicInfo.getData().getFeeds())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feedsBeans -> {
                    dynamics.addAll(feedsBeans);
                    finishTask();
                }, throwable -> initEmptyView());
    }


    @Override
    protected void finishTask() {
        mSwipeRefreshLayout.setRefreshing(false);
        mIsRefreshing = false;
        hideEmptyView();
        initRecyclerView();
    }

    @Override
    protected void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        AttentionDynamicAdapter mAdapter = new AttentionDynamicAdapter(mRecyclerView, dynamics);
        mRecyclerView.setAdapter(mAdapter);
        setRecycleNoScroll();
    }


    public void initEmptyView() {
        topLayout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.mipmap.img_tips_error_load_error);
        mCustomEmptyView.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
        SnackbarUtil.showMessage(mRecyclerView, "数据加载失败,请重新加载或者检查网络是否链接");
    }

    public void hideEmptyView() {
        topLayout.setVisibility(View.VISIBLE);
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void clearData() {
        mIsRefreshing = true;
        dynamics.clear();
    }

    private void setRecycleNoScroll() {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }
}
