package cn.zndroid.bili.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.zndroid.bili.R;
import cn.zndroid.bili.entity.search.SearchArchiveInfo;
import cn.zndroid.bili.net.RetrofitHelper;
import cn.zndroid.bili.ui.fragment.ArchiveResultsFragment;
import cn.zndroid.bili.ui.fragment.BangumiResultsFragment;
import cn.zndroid.bili.ui.fragment.MovieResultsFragment;
import cn.zndroid.bili.ui.fragment.UpperResultsFragment;
import cn.zndroid.bili.utils.ConstantUtil;
import cn.zndroid.bili.utils.KeyBoardUtil;
import cn.zndroid.bili.utils.StatusBarUtil;
import cn.zndroid.bili.widget.NoScrollViewPager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class TotalStationSearchActivity extends RxBaseActivity {

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.iv_search_loading)
    ImageView mLoadingView;
    @BindView(R.id.search_img)
    ImageView mSearchBtn;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_text_clear)
    ImageView mSearchTextClear;
    @BindView(R.id.search_layout)
    LinearLayout mSearchLayout;
    @BindView(R.id.search_back)
    ImageView mSearchBack;
    private String content;
    private AnimationDrawable mAnimationDrawable;
    private List<SearchArchiveInfo.DataBean.NavBean> navs = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initToolbar() {
       StatusBarUtil.from(this).setLightStatusBar(true).process();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Intent intent=getIntent();
        if(intent!=null){
            content=intent.getStringExtra(ConstantUtil.EXTRA_CONTENT);
        }
        mSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLoadingView.setImageResource(R.drawable.anim_search_loading);
        mAnimationDrawable= (AnimationDrawable) mLoadingView.getDrawable();
        showSearchAnim();
        mSearchEdit.clearFocus();
        mSearchEdit.setText(content);
        getSearchData();
        search();
        setUpEditText();
    }

    private void setUpEditText() {
        RxTextView.textChanges(mSearchEdit).
                compose(bindToLifecycle()).
                map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        return charSequence.toString();
                    }
                }).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if(!TextUtils.isEmpty(s)){
                            mSearchTextClear.setVisibility(View.VISIBLE);
                        }else {
                            mSearchTextClear.setVisibility(View.GONE);
                        }
                    }
                });

        RxView.clicks(mSearchTextClear).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mSearchEdit.setText("");
                    }
                });

         RxTextView.editorActions(mSearchEdit).
                filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return (!TextUtils.isEmpty(mSearchEdit.getText().toString().trim()));
                    }
                }).
                filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer == EditorInfo.IME_ACTION_SEARCH;
                    }
                }).
                flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.just(mSearchEdit.getText().toString().trim());
                    }
                }).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        KeyBoardUtil.closeKeybord(mSearchEdit, TotalStationSearchActivity.this);
                        showSearchAnim();
                        clearData();
                        content = s;
                        getSearchData();
                    }
                });
    }

    private void search() {
        RxView.clicks(mSearchBtn).
                throttleFirst(2,TimeUnit.SECONDS).
                map(new Func1<Void, String>() {
                    @Override
                    public String call(Void aVoid) {
                        return mSearchEdit.getText().toString().trim();
                    }
                }).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String o) {
                return !TextUtils.isEmpty(o);
            }
        }).observeOn(AndroidSchedulers.mainThread()).
        subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                KeyBoardUtil.closeKeybord(mSearchEdit, TotalStationSearchActivity.this);
                showSearchAnim();
                clearData();
                content = s;
                getSearchData();
            }
        });
    }

    private void clearData() {
        navs.clear();
        titles.clear();
        fragments.clear();
    }
    private void getSearchData() {
        int page = 1;
        int pageSize = 10;
        RetrofitHelper.getBiliAppAPI()
                .searchArchive(content, page, pageSize)
                .compose(this.bindToLifecycle())
                .map(new Func1<SearchArchiveInfo, SearchArchiveInfo.DataBean>() {
                    @Override
                    public SearchArchiveInfo.DataBean call(SearchArchiveInfo searchArchiveInfo) {
                        return searchArchiveInfo.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SearchArchiveInfo.DataBean>() {
                    @Override
                    public void call(SearchArchiveInfo.DataBean dataBean) {
                        navs.addAll(dataBean.getNav());
                        finishTask();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        setEmptyLayout();
                    }
                });
    }

    public void setEmptyLayout() {
        mLoadingView.setVisibility(View.VISIBLE);
        mSearchLayout.setVisibility(View.GONE);
        mLoadingView.setImageResource(R.mipmap.search_failed);
    }
    private void showSearchAnim() {
        mLoadingView.setVisibility(View.VISIBLE);
        mSearchLayout.setVisibility(View.GONE);
        mAnimationDrawable.start();
    }

    public static void launch(Activity activity,String str){
        Intent intent=new Intent(activity,TotalStationSearchActivity.class);
        intent.putExtra(ConstantUtil.EXTRA_CONTENT,str);
        activity.startActivity(intent);
    }

    @Override
    public void finishTask() {
        hideSearchAnim();
        titles.add("综合");
        titles.add(navs.get(0).getName() + "(" + checkNumResults(navs.get(0).getTotal()) + ")");
        titles.add(navs.get(1).getName() + "(" + checkNumResults(navs.get(1).getTotal()) + ")");
        titles.add(navs.get(2).getName() + "(" + checkNumResults(navs.get(2).getTotal()) + ")");

        ArchiveResultsFragment archiveResultsFragment = ArchiveResultsFragment.newInstance(content);
        BangumiResultsFragment bangumiResultsFragment = BangumiResultsFragment.newInstance(content);
        UpperResultsFragment upperResultsFragment = UpperResultsFragment.newInstance(content);
        MovieResultsFragment movieResultsFragment = MovieResultsFragment.newInstance(content);

        fragments.add(archiveResultsFragment);
        fragments.add(bangumiResultsFragment);
        fragments.add(upperResultsFragment);
        fragments.add(movieResultsFragment);

        SearchTabAdapter adapter=new SearchTabAdapter(getSupportFragmentManager(),titles,fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(titles.size());
        mSlidingTabLayout.setViewPager(mViewPager);
        measureTabLayoutTextWidth(0);
        mSlidingTabLayout.setCurrentTab(0);
        adapter.notifyDataSetChanged();
        mSlidingTabLayout.notifyDataSetChanged();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                measureTabLayoutTextWidth(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void measureTabLayoutTextWidth(int position) {
        String title = titles.get(position);
        TextView titleView = mSlidingTabLayout.getTitleView(position);
        TextPaint paint = titleView.getPaint();
        float v = paint.measureText(title);
        mSlidingTabLayout.setIndicatorWidth(v/3);
    }

    public String checkNumResults(int numResult) {
        return numResult > 100 ? "99+" : String.valueOf(numResult);
    }
    private void hideSearchAnim() {
        mLoadingView.setVisibility(View.GONE);
        mSearchLayout.setVisibility(View.VISIBLE);
        mAnimationDrawable.stop();
    }

    private static class SearchTabAdapter extends FragmentStatePagerAdapter{

        private List<String> titles;
        private List<Fragment> fragments;
        public SearchTabAdapter(FragmentManager fm,List<String> titles, List<Fragment> fragments) {
            super(fm);
            this.titles = titles;
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        if(mAnimationDrawable!=null&&mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
            mAnimationDrawable=null;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAnimationDrawable!=null){
            mAnimationDrawable.stop();
            mAnimationDrawable=null;
        }
    }
}
