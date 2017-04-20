package cn.yomii.www.projectmodel;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.DisplayMetrics;

import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.OkGo;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.logging.Level;

import cn.yomii.www.frame.ActivityLifeCallback;
import cn.yomii.www.projectmodel.net.http.HttpHelper;

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

            //单独使用CrashReport功能时
            //CrashReport.initCrashReport(this, Bugly_ID, BuildConfig.LOG_DEBUG);
            //CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
            //本地崩溃处理初始化
            CrashManager.getInstance().init();
            //Bugly初始化
            Beta.upgradeCheckPeriod = 60 * 1000;
            Bugly.init(this, Bugly_ID, BuildConfig.LOG_DEBUG);
            Bugly.setIsDevelopmentDevice(this, BuildConfig.DEBUG);


            OkGo.init(this);
            OkGo.getInstance()
                    .setConnectTimeout(HttpHelper.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                    .setReadTimeOut(HttpHelper.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                    .setWriteTimeOut(HttpHelper.DEFAULT_MILLISECONDS)               //全局的写入超时时间
                    .debug("OkGo", Level.INFO, true)    ;                            //是否打开调试
//                    .setCertificates(new Buffer().writeUtf8(CRT).inputStream());    //证书


        }
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


    /**
     * 微信Secret
     */
    public static final String WX_SECRET = "wx0798e1bdcaeca37c";

    /**
     * Bugly App ID
     */
    public static final String Bugly_ID = "todo";

    /**
     * 服务器自签名https证书
     */
    public static final String CRT = "todo";
}
