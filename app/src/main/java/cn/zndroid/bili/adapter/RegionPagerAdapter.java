package cn.zndroid.bili.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.zndroid.bili.entity.region.RegionTypesInfo;

/**
 * 分区界面PagerAdapter
 * Created by Administrator on 2018/3/26 0026.
 */

public class RegionPagerAdapter extends FragmentStatePagerAdapter {
    private int rid;
    private List<String> titles;
    private List<RegionTypesInfo.DataBean.ChildrenBean> childrens;
    private List<Fragment> fragments = new ArrayList<>();
    public RegionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
