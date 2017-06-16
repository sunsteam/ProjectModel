package cn.yomii.www.projectmodel.utils.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;


/**
 * 应用工具类
 * Created by Yomii on 2017/1/24.
 */
@SuppressWarnings("unused")
public class AppUtils {

    private AppUtils() {
    }

    /**
     * 检测包名是否存在
     *
     * @param pckName 包名
     *
     * @return {@code true}: 包名存在<br>{@code false}: 包名不存在
     */
    public static boolean isPackageExist(Context context, String pckName) {
        try {
            PackageInfo pckInfo = context.getPackageManager()
                    .getPackageInfo(pckName, 0);
            if (pckInfo != null)
                return true;
        } catch (PackageManager.NameNotFoundException e) {
            // TLog.error(e.getMessage());
        }
        return false;
    }


    /**
     * 静默安装App
     * <p>非root需添加权限 {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param filePath 文件路径
     *
     * @return {@code true}: 安装成功<br>{@code false}: 安装失败
     */
    public static boolean installAppSilent(Context context, String filePath) {
        File file = FileUtils.getFileByPath(filePath);
        if (!FileUtils.isFileExists(file))
            return false;
        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + filePath;
        ShellUtils.CommandResult commandResult =
                ShellUtils.execCmd(command, !isSystemApp(context,context.getPackageName()), true);
        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
    }


    /**
     * 静默卸载App
     * <p>非root需添加权限 {@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>
     *
     * @param context     上下文
     * @param packageName 包名
     * @param isKeepData  是否保留数据
     *
     * @return {@code true}: 卸载成功<br>{@code false}: 卸载成功
     */
    public static boolean uninstallAppSilent(Context context, String packageName, boolean isKeepData) {
        if (TextUtils.isEmpty(packageName))
            return false;
        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " + (isKeepData ? "-k " : "") + packageName;
        ShellUtils.CommandResult commandResult =
                ShellUtils.execCmd(command, !isSystemApp(context, context.getPackageName()), true);
        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
    }


    /**
     * 判断App是否有root权限
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppRoot() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("echo root", true);
        if (result.result == 0) {
            return true;
        }
        if (result.errorMsg != null) {
            Log.d("isAppRoot", result.errorMsg);
        }
        return false;
    }


    /**
     * 判断App是否是系统应用
     *
     * @param context     上下文
     * @param packageName 包名
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSystemApp(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
