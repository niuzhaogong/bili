package cn.zndroid.bili.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.zndroid.bili.R;
import cn.zndroid.bili.ui.fragment.HomeAttentionFragment;
import cn.zndroid.bili.ui.fragment.HomeBangumiFragment;
import cn.zndroid.bili.ui.fragment.HomeDiscoverFragment;
import cn.zndroid.bili.ui.fragment.HomeLiveFragment;
import cn.zndroid.bili.ui.fragment.HomeRecommendedFragment;
import cn.zndroid.bili.ui.fragment.HomeRegionFragment;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES;
    private Fragment[] fragments;
    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLES=context.getResources().getStringArray(R.array.sections);
        fragments=new Fragment[TITLES.length];
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments[position]==null){
            switch (position){
                case 0:
                    fragments[position] = HomeLiveFragment.newIntance();
                    break;
                case 1:
                    fragments[position] = HomeRecommendedFragment.newInstance();
                    break;
                case 2:
                    fragments[position] = HomeBangumiFragment.newInstance();
                    break;
                case 3:
                    fragments[position] = HomeRegionFragment.newInstance();
                    break;
                case 4:
                    fragments[position] = HomeAttentionFragment.newInstance();
                    break;
                case 5:
                    fragments[position] = HomeDiscoverFragment.newInstance();
                    break;
                default:
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
