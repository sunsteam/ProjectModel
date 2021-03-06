package com.yomii.core.bean.request;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.yomii.base.bean.RequestBean;
import com.yomii.core.BuildConfig;
import com.yomii.core.utils.SpfUtils;

import java.util.UUID;


/**
 * 登陆
 * Created by Yomii on 2016/6/30.
 */
public class LoginRequest extends RequestBean {

    public String imei;

    public String uid;

    public String pwd;

    public String productid = BuildConfig.APPLICATION_ID;

    /**
     * 用于正常登陆, imei会从spf获取, 没有则新建
     */
    public LoginRequest(String uid, String pwd) {
        super("app_login",null);
        this.uid = uid;
        this.pwd = pwd;

        setupImei();
    }

    private void setupImei() {
        SharedPreferences user = SpfUtils.getUser();
        imei = user.getString(SpfUtils.USER_IMEI, "");
        if (TextUtils.isEmpty(imei)) {
            imei = UUID.randomUUID().toString();
            user.edit().putString("uuid", imei).apply();
        }
    }


    /**
     * 只用于自动登录
     */
    private LoginRequest() {
        super("app_login",null);
        setupImei();
        SharedPreferences user = SpfUtils.getUser();
        this.pwd = user.getString(SpfUtils.USER_PWD, "");
        if (TextUtils.isEmpty(pwd)) {
            this.uid = "";
        } else {
            this.uid = user.getString(SpfUtils.USER_ID, "");
        }
    }

    public static LoginRequest getRetryLoginRequest() {
        return new LoginRequest();
    }
}
