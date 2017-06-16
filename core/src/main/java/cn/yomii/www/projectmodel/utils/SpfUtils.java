package cn.yomii.www.projectmodel.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import cn.yomii.www.projectmodel.App;

/**
 * Spf 相关工具类
 *
 * Created by Yomii on 2016/4/12.
 */
public class SpfUtils {

    /**
     * 用于app设置信息SPF文件名
     */
    public static final String SPF = "app";

    /**
     * 界面引导完成
     */
    public static final String SPF_GUIDE_READING = "guide";





    /**
     * 用于储存用户设置信息的文件名
     */
    public static final String USER = "user";

    /**
     * 储存临时用户身份证 uuid, 作为IMEI使用
     */
    public static final String USER_IMEI = "uuid";

    /**
     * 缓存用户上次的登录用户名
     */
    public static final String USER_ID = "user_id";

    /**
     * 缓存用户上次的登录用户名
     */
    public static final String USER_PWD = "user_pwd";




    public static SharedPreferences getSpf(String name){
        return App.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSpf() {
        return App.getContext().getSharedPreferences(SPF, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getUser() {
        return App.getContext().getSharedPreferences(USER, Context.MODE_PRIVATE);
    }

    public static void saveString(String key, String value){
        getSpf().edit().putString(key, value).apply();
    }

    public static void saveBoolean(String key, Boolean value){
        getSpf().edit().putBoolean(key, value).apply();
    }

    @SuppressLint("CommitPrefEdits")
    public static void saveStringSet(String key, Set<String> stdNoSet){
        getSpf().edit().putStringSet(key,null).commit();
        getSpf().edit().putStringSet(key,stdNoSet).apply();
    }

    public static void userSaveString(String key, String value){
        getUser().edit().putString(key, value).apply();
    }

    public static void userSaveBoolean(String key, Boolean value){
        getUser().edit().putBoolean(key, value).apply();
    }

    public static void userSaveStringSet(String key, Set<String> stdNoSet){
        getUser().edit().putStringSet(key,stdNoSet).apply();
    }


}
