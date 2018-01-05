package com.yomii.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.DisplayMetrics;

import com.apkfuns.logutils.LogUtils;
import com.letv.sarrsdesktop.blockcanaryex.jrt.BlockCanaryEx;
import com.letv.sarrsdesktop.blockcanaryex.jrt.Config;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.yomii.http_okgo.HttpHelper;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

import static com.yomii.core.ApiNeeds.Bugly_ID;

/**
 * 全局环境
 * Created by Yomii on 2017/1/9.
 */
public class App extends Application {

    private static App sContext;
    private static int sMainTid;
    private static Thread sMainThread;
    private static Handler sHandler;
    private static DisplayMetrics sMetrics;

    public static Context getContext() {
        return sContext;
    }

    /**
     * @return 主线程id
     */
    public static int getMainTid() {
        return sMainTid;
    }

    public static Thread getMainThread() {
        return sMainThread;
    }

    public static Handler getHandler() {
        return sHandler;
    }

    public static DisplayMetrics getMetrics() {
        return sMetrics;
    }

    /**
     * 在主线程运行任务
     */
    public static void runInMainThread(Runnable task) {
        if (getMainTid() == Process.myTid()) {
            task.run();
        } else {
            getHandler().post(task);
        }
    }


    private long startTime;


    @Override
    public void onCreate() {
        super.onCreate();

        String currentProcessName = getCurrentProcessName();
        LogUtils.i("onCreate currentProcessName=" + currentProcessName);
        startTime = System.currentTimeMillis();
        LogUtils.i("startTime __ " + startTime);

        if (getPackageName().equals(currentProcessName)) {
            BlockCanaryEx.install(new Config(this));
            LeakCanary.install(this);
            registerActivityLifecycleCallbacks(new ActivityLifeCallback() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    pageList.add(activity);
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    pageList.remove(activity);
                }
            });

            sMainTid = Process.myTid();
            sMainThread = Thread.currentThread();
            sHandler = new Handler();
            sContext = (App) getApplicationContext();
            sMetrics = sContext.getResources().getDisplayMetrics();

            LogUtils.w("逻辑密度: " + sMetrics.density);
            LogUtils.w("屏幕密度: " + sMetrics.densityDpi);
            LogUtils.w("屏幕宽度: " + sMetrics.widthPixels);
            LogUtils.w("屏幕高度: " + sMetrics.heightPixels);
            LogUtils.w("缩放密度: " + sMetrics.scaledDensity);

            //LogUtils初始化
            LogUtils.getLogConfig().configAllowLog(BuildConfig.LOG_DEBUG);
            initBuglyOrCrashReport();
            initWebClient();
        }
    }

    private void initBuglyOrCrashReport() {
        //单独使用CrashReport功能时
        //CrashReport.initCrashReport(this, Bugly_ID, BuildConfig.LOG_DEBUG);
        //CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
        //本地崩溃处理初始化
        CrashManager.getInstance().init();
        //Bugly初始化
        Beta.upgradeCheckPeriod = 60 * 1000;
        Bugly.init(this, Bugly_ID, BuildConfig.LOG_DEBUG);
        Bugly.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
    }

    private void initWebClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        builder.readTimeout(HttpHelper.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(HttpHelper.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(HttpHelper.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        //HTTPS方法三：使用预埋证书，校验服务端证书（自签名证书）
//        InputStream inputStream = new Buffer().writeUtf8(CRT).inputStream();
//        HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(inputStream);
//        builder.sslSocketFactory(sslParams3.sSLSocketFactory, sslParams3.trustManager);

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(1);                               //全局统一超时重连次数，默认为三次
    }


    private String getCurrentProcessName() {
        String currentProcessName = "";
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                currentProcessName = processInfo.processName;
                break;
            }
        }
        return currentProcessName;
    }

    /**
     * 打开的Activity引用链
     */
    private static ArrayList<Activity> pageList = new ArrayList<>();

    /**
     * 关闭Activity列表中的所有Activity
     */
    public static void finishActivity() {
        for (Activity activity : pageList) {
            if (null != activity) {
                activity.finish();
            }
        }
        pageList.clear();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
