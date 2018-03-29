package cn.zndroid.bili.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.zndroid.bili.R;
import cn.zndroid.bili.utils.ConstantUtil;
import cn.zndroid.bili.utils.PreferenceUtil;
import cn.zndroid.bili.utils.SystemStatusBarIsShowUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class SplashActivity extends RxAppCompatActivity {

    @BindView(R.id.splash_default_iv)
    ImageView mSplashDefaultIv;
    @BindView(R.id.splash_logo)
    ImageView mSplashLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        SystemStatusBarIsShowUtils.hideStatusBar(getWindow(),true);
        setUpSplash();
    }

    private void setUpSplash() {
        Observable.
                timer(2000, TimeUnit.MILLISECONDS).
                compose(this.<Long>bindToLifecycle()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        finishTask();
                    }
                });
    }

    private void finishTask() {
        boolean isLogin = PreferenceUtil.getBoolean(ConstantUtil.KEY, false);
        if(isLogin){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        }
        SplashActivity.this.finish();
    }
}
