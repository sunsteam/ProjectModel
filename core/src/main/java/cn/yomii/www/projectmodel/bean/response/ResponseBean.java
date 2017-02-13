package cn.yomii.www.projectmodel.bean.response;


import cn.yomii.www.projectmodel.bean.ModelEntity;

/**
 * 请求结果封装的基类
 * Created by Yomii on 2016/3/10.
 */
public class ResponseBean extends ModelEntity {

    /**
     * 状态码
     */
    public int err;

    /**
     * 错误信息
     */
    public String error;

    @Override
    public String toString() {
        return "ResponseBean{" +
                "err=" + err +
                ", error='" + error + '\'' +
                '}';
    }
}
