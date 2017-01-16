package cn.yomii.www.projectmodel.net.http;

/**
 * Created by Yomii on 2017/1/10.
 */

public class BusinessException extends RuntimeException {

    private int err;

    private String error;

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

    public BusinessException(int err, String error) {
        this.err = err;
        this.error = error;
    }
}
