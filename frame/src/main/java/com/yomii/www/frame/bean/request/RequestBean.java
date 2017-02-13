package com.yomii.www.frame.bean.request;


import com.yomii.www.frame.bean.ModelEntity;
import com.yomii.www.frame.net.http.Token;

/**
 * 请求封装基类
 * Created by Yomii on 2016/3/10.
 */
public class RequestBean extends ModelEntity {
    public String cmd;
    public String token;

    public RequestBean() {
    }

    public RequestBean(String cmd) {
        this.cmd = cmd;
        this.token = Token.getToken();
    }

    public RequestBean(String cmd, String token) {
        this.cmd = cmd;
        this.token = token;
    }
}
