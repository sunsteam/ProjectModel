package cn.yomii.www.frame.bean;


import java.util.List;

/**
 * 数据集请求封装基类
 * Created by Yomii on 2016/9/5.
 */
public class ListResponse<T>{

    /**
     * 本次返回数据的页码
     */
    private int pageIndex;

    private List<T> data;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ListResponse{" +
                "pageIndex=" + pageIndex +
                ", data=" + data +
                '}';
    }
}
