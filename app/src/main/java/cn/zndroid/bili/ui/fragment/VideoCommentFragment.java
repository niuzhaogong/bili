package cn.zndroid.bili.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import cn.zndroid.bili.R;
import cn.zndroid.bili.adapter.VideoCommentAdapter;
import cn.zndroid.bili.adapter.VideoHotCommentAdapter;
import cn.zndroid.bili.adapter.event.EndlessRecyclerOnScrollListener;
import cn.zndroid.bili.adapter.helper.HeaderViewRecyclerAdapter;
import cn.zndroid.bili.entity.video.VideoCommentInfo;
import cn.zndroid.bili.net.RetrofitHelper;
import cn.zndroid.bili.utils.ConstantUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class VideoCommentFragment extends RxLazyFragment{
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    private int aid;
    private int pageNum = 1;
    private int pageSize = 20;
    private View headView;
    private View loadMoreView;
    private HeaderViewRecyclerAdapter mAdapter;
    private VideoHotCommentAdapter mVideoHotCommentAdapter;
    private ArrayList<VideoCommentInfo.List> comments = new ArrayList<>();
    private ArrayList<VideoCommentInfo.HotList> hotComments = new ArrayList<>();


    public static VideoCommentFragment newInstance(int aid) {
        VideoCommentFragment fragment = new VideoCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtil.AID, aid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_video_comment;
    }

    @Override
    public void finishCreateView(Bundle state) {
        aid = getArguments().getInt(ConstantUtil.AID);
        initRecyclerView();
        loadData();
    }


    @Override
    protected void initRecyclerView() {
        VideoCommentAdapter mRecyclerAdapter = new VideoCommentAdapter(mRecyclerView, comments);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new HeaderViewRecyclerAdapter(mRecyclerAdapter);
        mRecyclerView.setAdapter(mAdapter);
        createHeadView();
        createLoadMoreView();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int i) {
                pageNum++;
                loadData();
                loadMoreView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void loadData() {
        int ver = 3;
        RetrofitHelper.getBiliAPI()
                .getVideoComment(aid, pageNum, pageSize, ver)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoComment -> {
                    ArrayList<VideoCommentInfo.List> list = videoComment.list;
                    ArrayList<VideoCommentInfo.HotList> hotList = videoComment.hotList;
                    if (list.size() < pageSize) {
                        loadMoreView.setVisibility(View.GONE);
                        mAdapter.removeFootView();
                    }
                    comments.addAll(list);
                    hotComments.addAll(hotList);
                    finishTask();
                }, throwable -> {
                    loadMoreView.setVisibility(View.GONE);
                    headView.setVisibility(View.GONE);
                });
    }


    @Override
    protected void finishTask() {
        loadMoreView.setVisibility(View.GONE);
        mVideoHotCommentAdapter.notifyDataSetChanged();

        if (pageNum * pageSize - pageSize - 1 > 0) {
            mAdapter.notifyItemRangeChanged(pageNum * pageSize - pageSize - 1, pageSize);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    private void createHeadView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_video_hot_comment_head, mRecyclerView, false);
        RecyclerView mHotCommentRecycler = (RecyclerView) headView.findViewById(R.id.hot_comment_recycler);
        mHotCommentRecycler.setHasFixedSize(false);
        mHotCommentRecycler.setNestedScrollingEnabled(false);
        mHotCommentRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVideoHotCommentAdapter = new VideoHotCommentAdapter(mHotCommentRecycler, hotComments);
        mHotCommentRecycler.setAdapter(mVideoHotCommentAdapter);
        mAdapter.addHeaderView(headView);
    }

    private void createLoadMoreView() {
        loadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_load_more, mRecyclerView, false);
        mAdapter.addFooterView(loadMoreView);
        loadMoreView.setVisibility(View.GONE);
    }
}
