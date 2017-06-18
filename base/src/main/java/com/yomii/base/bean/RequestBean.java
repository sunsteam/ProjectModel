package com.yomii.base.bean;


/**
 * 请求封装基类
 * Created by Yomii on 2016/3/10.
 */
public class RequestBean{
    public String cmd;
    public String token;

    public RequestBean() {
    }

    public RequestBean(String cmd, String token) {
        this.cmd = cmd;
        this.token = token;
    }
}
