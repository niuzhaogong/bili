package cn.zndroid.bili.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import butterknife.BindView;
import cn.zndroid.bili.R;
import cn.zndroid.bili.adapter.HomePagerAdapter;
import cn.zndroid.bili.ui.activitys.GameCentreActivity;
import cn.zndroid.bili.ui.activitys.MainActivity;
import cn.zndroid.bili.ui.activitys.OffLineDownloadActivity;
import cn.zndroid.bili.ui.activitys.TotalStationSearchActivity;
import cn.zndroid.bili.widget.CircleImageView;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class HomePageFragment extends RxLazyFragment {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTab;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.toolbar_user_avatar)
    CircleImageView mCircleImageView;
    @BindView(R.id.navigation_layout)
    LinearLayout mNavigation_layout;
    public static HomePageFragment newInstance(){
        return new HomePageFragment();
    }

    @Override
    protected void finishCreateView(Bundle savedInstanceState) {
        //fragment调用菜单时候必须执行的方法 一般在初始化时候调用
        mNavigation_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if(activity instanceof MainActivity){
                    ((MainActivity)activity).toggleDrawer();
                }
            }
        });
        setHasOptionsMenu(true);
        initToolBar();
        initSearchView();
        initViewPager();
    }

    private void initViewPager() {
        HomePagerAdapter adapter=new HomePagerAdapter(getChildFragmentManager(),getApplicationContext());
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setCurrentItem(1);
        mViewPager.setAdapter(adapter);
        mSlidingTab.setViewPager(mViewPager);
    }

    private void initSearchView() {
        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);
        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TotalStationSearchActivity.launch(getActivity(),query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initToolBar() {
        mToolbar.setTitle("");
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        mCircleImageView.setImageResource(R.mipmap.ic_dandan);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main,menu);
        // 设置SearchViewItemMenu
        MenuItem item = menu.findItem(R.id.id_action_search);
        mSearchView.setMenuItem(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.id_action_game:
                startActivity(new Intent(getActivity(), GameCentreActivity.class));
                break;
            case R.id.id_action_download:
                startActivity(new Intent(getActivity(), OffLineDownloadActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOpenSearchView() {
        return mSearchView.isSearchOpen();
    }


    public void closeSearchView() {
        mSearchView.closeSearch();
    }
}
