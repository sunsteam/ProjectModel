package cn.yomii.www.projectmodel.bean.response;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

import cn.yomii.www.frame.bean.ListResponse;

/**
 * 请求结果封装的基类
 * Created by Yomii on 2016/3/10.
 */
public class ListResponseBean<T> extends ListResponse<T> {

    /**
     * 状态码
     */
    private int err;

    /**
     * 错误信息
     */
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

    @Override
    @JSONField(name = "pageindex")
    public int getPageIndex() {
        return super.getPageIndex();
    }

    @Override
    @JSONField(name = "pageindex")
    public void setPageIndex(int pageIndex) {
        super.setPageIndex(pageIndex);
    }

    @Override
    @JSONField(name = "records")
    public List<T> getData() {
        return super.getData();
    }

    @Override
    @JSONField(name = "records")
    public void setData(List<T> data) {
        super.setData(data);
    }

    @Override
    public String toString() {
        return "ListResponseBean{" +
                "err=" + err +
                ", error='" + error + '\'' +
                "} " + super.toString();
    }
}
