package cn.yomii.www.projectmodel.bean.response;

import java.util.List;

/**
 * Created by Yomii on 2017/6/15.
 *
 * @param <M> ServerModel
 */

public class ListResponse<M> extends ResponseBean {

    private int index;

    private int size;

    private List<M> data;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<M> getData() {
        return data;
    }

    public void setData(List<M> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return super.toString() + "ListResponse{" +
                "index=" + index +
                ", size=" + size +
                ", data=" + (data == null ? "null" : data.size()) +
                "} ";
    }
}
