package cn.yomii.www.projectmodel.bean.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.UUID;

import cn.yomii.www.projectmodel.App;
import cn.yomii.www.projectmodel.BuildConfig;
import cn.yomii.www.projectmodel.Info;
import cn.yomii.www.projectmodel.utils.SpfUtils;

/**
 * Created by Yomii on 2016/6/30.
 */
public class LoginRequest extends RequestBean {

    public String app = "aphone";

    public int ver = 1;

    public int os = 1;

    public String ip;

    public String mac;

    public String imei;

    public String csmid;

    public String uid;

    public String pwd;

    public String productid = BuildConfig.APPLICATION_ID;

    /**
     * 用于正常登陆, imei会从spf获取, 没有则新建
     *
     * @param csmid
     * @param uid
     * @param pwd
     */
    public LoginRequest(String csmid, String uid, String pwd) {
        super("app_login");
        this.csmid = csmid;
        this.uid = uid;
        this.pwd = pwd;

        SharedPreferences main = App.getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        imei = main.getString("uuid", "");
        if (TextUtils.isEmpty(imei)) {
            imei = UUID.randomUUID().toString();
            main.edit().putString("uuid", imei).apply();
        }
    }


    /**
     * 只用于自动重试登录
     */
    private LoginRequest() {
        super("app_login");
        this.imei = Info.getImei();
        SharedPreferences spf = SpfUtils.getSpf("longinvalue");
        this.pwd = spf.getString("txt2", "");
        if (TextUtils.isEmpty(pwd)) {
            this.csmid = "";
            this.uid = "";
        } else {
            this.csmid = spf.getString("txt", "");
            this.uid = spf.getString("txt1", "");
        }
    }

    public static LoginRequest getRetryLoginRequest(){
        return new LoginRequest();
    }
}
