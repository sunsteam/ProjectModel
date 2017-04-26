package cn.yomii.www.projectmodel.bean.response;


/**
 * 请求结果封装的基类
 * Created by Yomii on 2016/3/10.
 */
public class ResponseBean{

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
