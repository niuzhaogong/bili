package cn.zndroid.bili.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import cn.zndroid.bili.R;
import cn.zndroid.bili.adapter.LiveAppIndexAdapter;
import cn.zndroid.bili.net.RetrofitHelper;
import cn.zndroid.bili.utils.SnackbarUtil;
import cn.zndroid.bili.widget.CustomEmptyView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class HomeLiveFragment extends RxLazyFragment {

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    private LiveAppIndexAdapter mLiveAppIndexAdapter;

    public static HomeLiveFragment newIntance() {
        return new HomeLiveFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_live;
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
        initRecyclerView();
        isPrepared = false;
    }

    @Override
    protected void initRecyclerView() {
        mLiveAppIndexAdapter = new LiveAppIndexAdapter(getActivity());
        mRecyclerView.setAdapter(mLiveAppIndexAdapter);
        GridLayoutManager layout = new GridLayoutManager(getActivity(), 12);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mLiveAppIndexAdapter.getSpanSize(position);
            }
        });
        mRecyclerView.setLayoutManager(layout);
    }


    @Override
    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this::loadData);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            loadData();
        });
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getLiveAPI()
                .getLiveAppIndex()
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(liveAppIndexInfo -> {
                    mLiveAppIndexAdapter.setLiveInfo(liveAppIndexInfo);
                    finishTask();
                }, throwable -> initEmptyView());
    }


    private void initEmptyView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.mipmap.img_tips_error_load_error);
        mCustomEmptyView.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
        SnackbarUtil.showMessage(mRecyclerView, "数据加载失败,请重新加载或者检查网络是否链接");
    }


    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void finishTask() {
        hideEmptyView();
        mSwipeRefreshLayout.setRefreshing(false);
        mLiveAppIndexAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }
}
