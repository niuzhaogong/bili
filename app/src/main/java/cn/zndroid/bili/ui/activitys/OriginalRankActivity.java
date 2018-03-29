package cn.zndroid.bili.ui.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import cn.zndroid.bili.R;
import cn.zndroid.bili.ui.fragment.OriginalRankFragment;
import cn.zndroid.bili.widget.NoScrollViewPager;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class OriginalRankActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager mViewPager;

    private String[] titles = new String[]{"原创", "全站", "番剧"};
    private String[] orders = new String[]{"origin-03.json", "all-03.json", "all-3-33.json"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_original_rank;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mViewPager.setAdapter(new OriginalRankPagerAdapter(getSupportFragmentManager(), titles, orders));
        mViewPager.setOffscreenPageLimit(orders.length);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void initToolbar() {
        mToolbar.setTitle("排行榜");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rank, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private static class OriginalRankPagerAdapter extends FragmentStatePagerAdapter {
        private String[] titles;
        private String[] orders;

        OriginalRankPagerAdapter(FragmentManager fm, String[] titles, String[] orders) {
            super(fm);
            this.titles = titles;
            this.orders = orders;
        }

        @Override
        public Fragment getItem(int position) {
            return OriginalRankFragment.newInstance(orders[position]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
