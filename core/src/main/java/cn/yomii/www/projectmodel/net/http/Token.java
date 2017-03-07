package cn.yomii.www.projectmodel.net.http;

/**
 * Token
 * Created by Yomii on 2017/2/13.
 */

public class Token {

    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Token.token = token;
    }
}
