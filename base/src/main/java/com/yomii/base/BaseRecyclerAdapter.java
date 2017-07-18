package com.yomii.base;


import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yomii on 2016/6/14.
 *
 * RecyclerView 基类, 包含数据容器, 扩充数据加载方式, 封装分页请求所需要的元数据, 外部获取数据后可调用 onFilteredSuccess
 * 或 setState 方法更新分页描述信息
 *
 * @param <D> 数据包装类型
 */
public abstract class BaseRecyclerAdapter<D, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>{

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

}