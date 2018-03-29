package cn.zndroid.bili.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.stetho.common.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.zndroid.bili.R;
import cn.zndroid.bili.adapter.OriginalRankAdapter;
import cn.zndroid.bili.entity.discover.OriginalRankInfo;
import cn.zndroid.bili.net.RetrofitHelper;
import cn.zndroid.bili.ui.activitys.VideoDetailsActivity;
import cn.zndroid.bili.utils.ConstantUtil;
import cn.zndroid.bili.utils.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class OriginalRankFragment extends RxLazyFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    private String order;
    private boolean mIsRefreshing = false;
    private OriginalRankAdapter mAdapter;
    private List<OriginalRankInfo.RankBean.ListBean> originalRanks = new ArrayList<>();

    public static OriginalRankFragment newInstance(String order) {
        OriginalRankFragment mFragment = new OriginalRankFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtil.EXTRA_ORDER, order);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_original_rank;
    }


    @Override
    public void finishCreateView(Bundle state) {
        order = getArguments().getString(ConstantUtil.EXTRA_ORDER);
        initRefreshLayout();
        initRecyclerView();
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
            mIsRefreshing = true;
            originalRanks.clear();
            loadData();
        });
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getRankAPI()
                .getOriginalRanks(order)
                .compose(this.bindToLifecycle())
                .map(originalRankInfo -> originalRankInfo.getRank().getList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listBeans -> {
                    originalRanks.addAll(listBeans.subList(0, 20));
                    finishTask();
                }, throwable -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    LogUtil.e(throwable.getMessage());
                    ToastUtil.ShortToast("加载失败啦,请重新加载~");
                });
    }


    @Override
    protected void finishTask() {
        mIsRefreshing = false;
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new OriginalRankAdapter(mRecyclerView, originalRanks);
        mRecyclerView.setAdapter(mAdapter);
        setRecycleNoScroll();
        mAdapter.setOnItemClickListener((position, holder) -> VideoDetailsActivity.
                launch(getActivity(), originalRanks.get(position).getAid(),
                        originalRanks.get(position).getPic()));
    }

    private void setRecycleNoScroll() {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }
}
