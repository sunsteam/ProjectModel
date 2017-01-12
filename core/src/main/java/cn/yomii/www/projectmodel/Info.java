package cn.yomii.www.projectmodel;

import cn.yomii.www.projectmodel.bean.response.LoginResponse;

/**
 * Created by Yomii on 2017/1/10.
 */

public class Info {

    private static String sImei = "";
    private static String sToken = "";
    private static int sKind;
    private static int sPsnNo;
    private static String sPsnUid = "";

    public static String getToken() {
        return sToken;
    }

    public static void setToken(String token) {
        sToken = token;
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

    public static void setPsnNo(int sPsnNo) {
        Info.sPsnNo = sPsnNo;
    }

    public static String getPsnUid() {
        return sPsnUid;
    }

    public static void setPsnUid(String sPsnUid) {
        Info.sPsnUid = sPsnUid;
    }


    public static void fillFromResponse(LoginResponse response){
        sToken = response.token;
        sPsnNo = response.no;
        sPsnUid= response.uid;
        sKind= response.kind;
    }

    public static void fillFromCache() {
        // TODO: 2017/1/10
    }
}
