package com.yomii.core;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.yomii.core.utils.common.FileUtils;
import com.yomii.core.utils.common.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 获取崩溃信息并保存在本地
 * Created by Yomii on 2016/1/24.
 */
public class CrashManager implements Thread.UncaughtExceptionHandler {

    private static CrashManager crashUtilsInstance;

    private Thread.UncaughtExceptionHandler defaultHandler;

    private boolean initialized;
    private String crashDir;

    private CrashManager() {
    }

    /**
     * 获取单例
     * <p>在Application中初始化{@code CrashManager.getInstance().init(this);}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}</p>
     *
     * @return 单例
     */
    public static CrashManager getInstance() {
        if (crashUtilsInstance == null) {
            synchronized (CrashManager.class) {
                if (crashUtilsInstance == null) {
                    crashUtilsInstance = new CrashManager();
                }
            }
        }
        return crashUtilsInstance;
    }

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init() {
        if (initialized)
            return true;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File baseCache = App.getContext().getExternalCacheDir();
            if (baseCache == null)
                return getDirPathFailed();
            crashDir = baseCache.getPath() + File.separator + "crash" + File.separator;
        } else {
            File baseCache = App.getContext().getCacheDir();
            if (baseCache == null)
                return getDirPathFailed();
            crashDir = baseCache.getPath() + File.separator + "crash" + File.separator;
        }
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return initialized = true;
    }

    private boolean getDirPathFailed() {
        Log.e("CrashManager", "Get Crash Dir Path Failed");
        return false;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        printExceptionIntoFile(throwable);
        if (defaultHandler != null)
            defaultHandler.uncaughtException(thread, throwable);
        //        if (!BuildConfig.LOG_DEBUG) {
        //            Intent intent = new Intent(App.getContext(), SplashActivity.class);
        //            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //            PendingIntent restartIntent = PendingIntent.getActivity(
        //                    App.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //            //退出程序
        //            AlarmManager mgr = (AlarmManager) App.getContext().getSystemService(Context.ALARM_SERVICE);
        //            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 150, restartIntent);
        //            // 1秒钟后重启应用
        //            App.finishActivity();
        //        }

    }

    /**
     * 输出崩溃信息到文件
     *
     * @param throwable 崩溃详情
     */
    private void printExceptionIntoFile(final Throwable throwable) {
        String now = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        final String fullPath = crashDir + now + ".txt";
        if (!FileUtils.createOrExistsFile(fullPath)) {
            Log.e("CrashManager", "Crash File Not Exist And Can't Be Created");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, false));
                    pw.write(getCrashHead());
                    throwable.printStackTrace(pw);
                    Throwable cause = throwable.getCause();
                    while (cause != null) {
                        cause.printStackTrace(pw);
                        cause = cause.getCause();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.close(pw);
                }
            }
        }).start();
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        return "\n************* Crash Log Head ****************" +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                "\nApp VersionName    : " + BuildConfig.VERSION_NAME +
                "\nApp VersionCode    : " + BuildConfig.VERSION_CODE +
                "\nApp UserId         : " + Info.getPsnUid() +// 用户ID
                "\n************* Crash Log Head ****************\n\n";
    }
}