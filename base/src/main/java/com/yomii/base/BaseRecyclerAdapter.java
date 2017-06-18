package com.yomii.base;


import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yomii on 2016/6/14.
 *
 * RecyclerView 基类, 包含数据容器, 扩充数据加载方式, 封装分页请求所需要的元数据, 外部获取数据后可调用 onLoadSuccess
 * 或 setState 方法更新分页描述信息
 *
 * @param <D> 数据包装类型
 */
public abstract class BaseRecyclerAdapter<D, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements LoaderState {

    protected List<D> dataList;

    public BaseRecyclerAdapter() {
        this(null);
    }

    public BaseRecyclerAdapter(List<D> dataList) {
        this.dataList = (dataList == null ? new ArrayList<D>() : dataList);
        setHasStableIds(true);
    }

    public List<D> getDataList() {
        return dataList;
    }

    public void setDataList(List<D> dataList) {
        if (dataList != null) {
            this.dataList = dataList;
            notifyDataSetChanged();
        }
    }

    public void addDataFromList(List<D> dataList) {
        if (dataList != null) {
            int start = this.dataList.size();
            this.dataList.addAll(dataList);
            notifyItemRangeInserted(start, dataList.size());
        }
    }

    public void addData(D data) {
        if (data != null) {
            int start = this.dataList.size();
            this.dataList.add(data);
            notifyItemInserted(start);
        }
    }

    public void addData(int position, D data) {
        if (data != null && position > -1 && (position < dataList.size() || position == 0)) {
            this.dataList.add(position, data);
            notifyItemInserted(position);
        }
    }

    public void removeData(D data) {
        if (dataList.contains(data)) {
            int position = dataList.indexOf(data);
            this.dataList.remove(data);
            notifyItemRemoved(position);
            if (position == 0)
                notifyItemChanged(0);
        }
    }

    public void removeData(int position) {
        if (position < dataList.size() && position > -1) {
            dataList.remove(position);
            notifyItemRemoved(position);
            if (position == 0)
                notifyItemChanged(0);
        }
    }

    public void clearDataList() {
        dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    /*________________________________________分页请求部分_________________________________________*/


    /**
     * 数据状态
     */
    protected int state = LoaderState.STATE_HASMORE;

    /**
     * 每页数据条数
     */
    protected int pageSize = 15;

    /**
     * 当前数据索引
     */
    protected int index = 0;


    /**
     * @param data 新增数据 根据返回的data内容更新数据列表
     *
     * @return 是否已经 notify adapter change ， true: 是 ； false： 否
     */
    public boolean onLoadSuccess(List<D> data) {
        if (data != null && data.size() > 0) {
            state = data.size() < pageSize ? LoaderState.STATE_NOMORE : LoaderState.STATE_HASMORE;
            //List增加数据
            addDataFromList(data);
            return true;
        } else {
            state = dataList.size() > 0 ? LoaderState.STATE_NOMORE : LoaderState.STATE_EMPTY;
            return false;
        }
    }


    /**
     * @return 当前数据状态
     */
    @State
    public int getState() {
        return state;
    }

    /**
     * @param state 设置数据状态
     */
    public void setState(@State int state) {
        this.state = state;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int size) {
        this.pageSize = size;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}