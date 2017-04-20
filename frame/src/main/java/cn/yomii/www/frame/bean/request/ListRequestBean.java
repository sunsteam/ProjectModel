package cn.yomii.www.frame.bean.request;


/**
 * 数据集合请求封装
 * Created by Yomii on 2016/9/5.
 * @param <R>   具体请求数据封装
 */
public class ListRequestBean<R> {

    private int pageIndex;

    private int pageSize;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    R request;

    public R getRequest() {
        return request;
    }

    public void setRequest(R request) {
        this.request = request;
    }

    public ListRequestBean(int pageIndex, int pageSize) {
        this(pageIndex, pageSize, null);
    }

    public ListRequestBean(int pageIndex, int pageSize, R request) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.request = request;
    }
}
