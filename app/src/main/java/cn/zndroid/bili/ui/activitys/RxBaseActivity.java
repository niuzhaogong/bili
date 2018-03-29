package cn.zndroid.bili.ui.activitys;

import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public abstract class RxBaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initViews(savedInstanceState);
        initToolbar();
    }

    protected abstract int getLayoutId();

    protected abstract void initToolbar();

    protected abstract void initViews(Bundle savedInstanceState);

    public void loadData(){}

    public void showProgressBar(){}

    public void hideProgressBar(){}

    public void initRecyclerView(){}

    public void initRefreshView(){}

    public void finishTask(){}
}
