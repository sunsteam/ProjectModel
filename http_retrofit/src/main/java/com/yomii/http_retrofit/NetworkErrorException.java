package com.yomii.http_retrofit;

/**
 * Created by Yomii on 2017/6/19.
 * <p>
 * 网络连接失败 code < 200 或 code > 300
 */

public class NetworkErrorException extends Exception {

    private int code;

    public NetworkErrorException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
