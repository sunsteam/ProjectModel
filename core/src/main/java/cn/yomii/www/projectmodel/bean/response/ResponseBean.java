package cn.yomii.www.projectmodel.bean.response;


/**
 * 请求结果封装的基类
 * Created by Yomii on 2016/3/10.
 */
public class ResponseBean{

    /**
     * 状态码
     */
    private int err;

    /**
     * 错误信息
     */
    private String error;

    @Override
    public String toString() {
        return "ResponseBean{" +
                "err=" + err +
                ", error='" + error + '\'' +
                '}';
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
