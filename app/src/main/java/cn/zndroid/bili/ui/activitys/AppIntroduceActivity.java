package cn.zndroid.bili.ui.activitys;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.zndroid.bili.R;
import cn.zndroid.bili.utils.ShareUtil;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class AppIntroduceActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_version)
    TextView mVersion;
    @BindView(R.id.tv_network_diagnosis)
    TextView mTvNetworkDiagnosis;


    @Override
    public int getLayoutId() {
        return R.layout.activity_app_introduce;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initViews(Bundle savedInstanceState) {
        mVersion.setText("v" + getVersion());
    }


    @Override
    public void initToolbar() {
        mToolbar.setTitle("关于");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private String getVersion() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return getString(R.string.about_version);
        }
    }

    @OnClick({R.id.tv_share_app, R.id.tv_feedback})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_share_app:
                //分享app
                ShareUtil.shareLink(getString(R.string.github_url),
                        getString(R.string.share_title), AppIntroduceActivity.this);
                break;
            case R.id.tv_feedback:
                //意见反馈
                new AlertDialog.Builder(AppIntroduceActivity.this)
                        .setTitle(R.string.feedback_titlle)
                        .setMessage(R.string.feedback_message)
                        .setPositiveButton("确定", (dialog, which) -> dialog.dismiss())
                        .show();
                break;
        }
    }
}
