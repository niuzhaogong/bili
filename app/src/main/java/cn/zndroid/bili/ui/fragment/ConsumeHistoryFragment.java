package cn.zndroid.bili.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import cn.zndroid.bili.R;
import cn.zndroid.bili.ui.activitys.MainActivity;
import cn.zndroid.bili.widget.CustomEmptyView;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class ConsumeHistoryFragment extends RxLazyFragment {
    @BindView(R.id.empty_view)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static ConsumeHistoryFragment newInstance() {
        return new ConsumeHistoryFragment();
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_empty;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mToolbar.setTitle("我的钱包");
        mToolbar.setNavigationIcon(R.mipmap.ic_navigation_drawer);
        mToolbar.setNavigationOnClickListener(v -> {
            Activity activity1 = getActivity();
            if (activity1 instanceof MainActivity) {
                ((MainActivity) activity1).toggleDrawer();
            }
        });
        mCustomEmptyView.setEmptyImage(R.mipmap.ic_movie_pay_area_limit);
        mCustomEmptyView.setEmptyText("你还没有消费记录哟");
    }
}
