package cn.yomii.www.projectmodel;

import com.tencent.bugly.crashreport.CrashReport;

import cn.yomii.www.projectmodel.bean.response.LoginResponse;

/**
 * 储存用户相关全局信息
 * Created by Yomii on 2017/1/10.
 */
public class Info {

    /**
     * 临时用户UUID
     */
    private static String sImei = "";
    /**
     * 服务器会话token
     */
    private static String sToken = "";
    /**
     * 用户类型
     */
    private static int sKind;
    /**
     * 用户唯一编号
     */
    private static int sPsnNo;
    /**
     * 用户ID
     */
    private static String sPsnUid = "";

    public static String getToken() {
        return sToken;
    }

    public static void setToken(String token) {
        sToken = token;
        CrashReport.putUserData(App.getContext(),"token",sToken);
    }

    public static String getImei() {return sImei;}

    public static void setImei(String imei) {sImei = imei;}

    public static void setKind(int kind) {
        sKind = kind;
    }

    public static boolean isTemperToken() {
        return sKind == 0;
    }

    public static boolean isPersonalToken() {
        return sKind == 1;
    }

    public static boolean isCompanyToken() {
        return sKind > 1;
    }

    public static int getPsnNo() {
        return sPsnNo;
    }

    public static void setPsnNo(int userNo) {
        sPsnNo = userNo;
    }

    public static String getPsnUid() {
        return sPsnUid;
    }

    public static void setPsnUid(String userId) {
        sPsnUid = userId;
        CrashReport.setUserId(sPsnUid);
    }


    public static void fillFromResponse(LoginResponse response){
        setToken(response.token);
        setPsnNo(response.no);
        setPsnUid(response.uid);
        setKind(response.kind);
    }

    public static void fillFromCache() {
        // TODO: 2017/1/10
    }
}
