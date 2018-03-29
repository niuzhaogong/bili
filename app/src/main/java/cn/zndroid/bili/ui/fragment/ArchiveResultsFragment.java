package cn.zndroid.bili.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.zndroid.bili.R;
import cn.zndroid.bili.adapter.ArchiveHeadBangumiAdapter;
import cn.zndroid.bili.adapter.ArchiveResultsAdapter;
import cn.zndroid.bili.adapter.event.EndlessRecyclerOnScrollListener;
import cn.zndroid.bili.adapter.helper.HeaderViewRecyclerAdapter;
import cn.zndroid.bili.entity.search.SearchArchiveInfo;
import cn.zndroid.bili.net.RetrofitHelper;
import cn.zndroid.bili.ui.activitys.VideoDetailsActivity;
import cn.zndroid.bili.utils.ConstantUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 综合搜索结果界面
 * Created by Administrator on 2018/3/26 0026.
 */

public class ArchiveResultsFragment extends RxLazyFragment {
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    ImageView mEmptyView;
    @BindView(R.id.iv_search_loading)
    ImageView mLoadingView;

    private String content;
    private int pageNum = 1;
    private int pageSize = 10;
    private View loadMoreView;
    private List<SearchArchiveInfo.DataBean.ItemsBean.ArchiveBean> archives = new ArrayList<>();
    private List<SearchArchiveInfo.DataBean.ItemsBean.SeasonBean> seasons = new ArrayList<>();
    private ArchiveHeadBangumiAdapter archiveHeadBangumiAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    public static ArchiveResultsFragment newInstance(String content){
        ArchiveResultsFragment fragment=new ArchiveResultsFragment();
        Bundle bundle=new Bundle();
        bundle.putString(ConstantUtil.EXTRA_CONTENT,content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void finishCreateView(Bundle savedInstanceState) {
        content=getArguments().getString(ConstantUtil.EXTRA_CONTENT);
        isPrepared=true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initRecyclerView();
        loadData();
        isPrepared = false;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_archive_results;
    }

    @Override
    protected void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(LinearLayoutManager);
        ArchiveResultsAdapter mAdapter=new ArchiveResultsAdapter(mRecyclerView,archives);
        mHeaderViewRecyclerAdapter=new HeaderViewRecyclerAdapter(mAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        createHeadView();
        createLoadMoreView();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(LinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                pageNum++;
                loadData();
                loadMoreView.setVisibility(View.VISIBLE);
            }
        });
        mAdapter.setOnItemClickListener((position, holder) -> {
            SearchArchiveInfo.DataBean.ItemsBean.ArchiveBean archiveBean = archives.get(position);
            VideoDetailsActivity.launch(getActivity(), Integer.valueOf(archiveBean.getParam()),
                    archiveBean.getCover());
        });
    }

    private void createHeadView() {
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_search_archive_head_view, mRecyclerView, false);
        RecyclerView mHeadBangumiRecycler = (RecyclerView) headView.findViewById(R.id.search_archive_bangumi_head_recycler);
        mHeadBangumiRecycler.setHasFixedSize(false);
        mHeadBangumiRecycler.setNestedScrollingEnabled(false);
        mHeadBangumiRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        archiveHeadBangumiAdapter = new ArchiveHeadBangumiAdapter(mHeadBangumiRecycler, seasons);
        mHeadBangumiRecycler.setAdapter(archiveHeadBangumiAdapter);
        mHeaderViewRecyclerAdapter.addHeaderView(headView);
    }

    @Override
    protected void loadData() {
        RetrofitHelper.getBiliAppAPI().searchArchive(content,pageNum,pageSize).
                compose(bindToLifecycle()).
                map(new Func1<SearchArchiveInfo, SearchArchiveInfo.DataBean>() {
                    @Override
                    public SearchArchiveInfo.DataBean call(SearchArchiveInfo searchArchiveInfo) {
                        return searchArchiveInfo.getData();
                    }
                }).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                doOnNext(new Action1<SearchArchiveInfo.DataBean>() {
                    @Override
                    public void call(SearchArchiveInfo.DataBean dataBean) {
                        if(dataBean.getItems().getArchive().size()<pageSize){
                            loadMoreView.setVisibility(View.GONE);
                            mHeaderViewRecyclerAdapter.removeFootView();
                        }
                    }
                }).subscribe(new Action1<SearchArchiveInfo.DataBean>() {
            @Override
            public void call(SearchArchiveInfo.DataBean dataBean) {
                archives.addAll(dataBean.getItems().getArchive());
                seasons.addAll(dataBean.getItems().getSeason());
                finishTask();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                showEmptyView();
                loadMoreView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void finishTask() {
        if(archives!=null){
            if(archives.size()==0){
                showEmptyView();
            }else {
                hideEmptyView();
            }
        }
        loadMoreView.setVisibility(View.GONE);
        archiveHeadBangumiAdapter.notifyDataSetChanged();
        if (pageNum * pageSize - pageSize - 1 > 0) {
            mHeaderViewRecyclerAdapter.notifyItemRangeChanged(pageNum * pageSize - pageSize - 1, pageSize);
        } else {
            mHeaderViewRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void hideEmptyView() {
        mEmptyView.setVisibility(View.GONE);
    }

    private void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void createLoadMoreView(){
        loadMoreView= LayoutInflater.from(getActivity()).inflate(R.layout.layout_load_more,mRecyclerView,false);
        mHeaderViewRecyclerAdapter.addFooterView(loadMoreView);
        loadMoreView.setVisibility(View.GONE);
    }
}
