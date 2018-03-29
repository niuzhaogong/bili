package cn.zndroid.bili;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class MyApp extends Application {

    public static MyApp mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        init();
    }

    private void init() {
        LeakCanary.install(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this).
                        enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).
                        enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).
                        build()
        );
    }

    public static MyApp getmInstance(){
        return mInstance;
    }
}
