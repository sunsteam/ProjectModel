package cn.yomii.www.projectmodel.bean.request;


import cn.yomii.www.frame.bean.ModelEntity;

/**
 * 请求封装基类
 * Created by Yomii on 2016/3/10.
 */
public class RequestBean extends ModelEntity {
    public String cmd;
    public String token;

    public RequestBean() {
    }

    public RequestBean(String cmd, String token) {
        this.cmd = cmd;
        this.token = token;
    }
}
