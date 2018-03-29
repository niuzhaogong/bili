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

public class IFavoritesFragment extends RxLazyFragment {
    @BindView(R.id.empty_view)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static IFavoritesFragment newInstance() {
        return new IFavoritesFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_empty;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mToolbar.setTitle("我的收藏");
        mToolbar.setNavigationIcon(R.mipmap.ic_navigation_drawer);
        mToolbar.setNavigationOnClickListener(v -> {
            Activity activity1 = getActivity();
            if (activity1 instanceof MainActivity) {
                ((MainActivity) activity1).toggleDrawer();
            }
        });
        mCustomEmptyView.setEmptyImage(R.mipmap.img_tips_error_fav_no_data);
        mCustomEmptyView.setEmptyText("没有找到你的收藏哟");
    }
}
