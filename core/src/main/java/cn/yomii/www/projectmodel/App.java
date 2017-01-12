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

import java.util.ArrayList;
import java.util.logging.Level;

import cn.yomii.www.projectmodel.net.http.HttpHelper;
import okio.Buffer;

/**
 * Created by Yomii on 2017/1/9.
 */

public class App extends Application {

    private static Context sContext;
    private static int sMainTid;
    private static Thread sMainThread;
    private static Handler sHandler;
    private static DisplayMetrics sMetrics;

    public static Context getContext() {
        return sContext;
    }

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

            sContext = getApplicationContext();
            sMainTid = Process.myTid();
            sMainThread = Thread.currentThread();
            sHandler = new Handler();
            sMetrics = sContext.getResources().getDisplayMetrics();

            LogUtils.getLogConfig().configAllowLog(BuildConfig.LOG_DEBUG);

            //设置统计超时时间为1分钟
            //MobclickAgent.setSessionContinueMillis(60000);
            //MobclickAgent.setDebugMode(BuildConfig.LOG_DEBUG);

            OkGo.init(this);
            OkGo.getInstance()
                    .setConnectTimeout(HttpHelper.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                    .setReadTimeOut(HttpHelper.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                    .setWriteTimeOut(HttpHelper.DEFAULT_MILLISECONDS)               //全局的写入超时时间
                    .debug("OkGo", Level.INFO, true)                                //是否打开调试
                    .setCertificates(new Buffer().writeUtf8(CRT).inputStream());    //证书

            LogUtils.w("密度: "+ sMetrics.density);
            LogUtils.w("屏宽: "+ getResources().getDisplayMetrics().widthPixels);
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
     * 服务器自签名https证书
     */
    public static final String CRT = "";
}
