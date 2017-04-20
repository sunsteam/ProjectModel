package cn.yomii.www.frame.base;


import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * RecyclerView 基类
 *
 * @param <T> 数据包装类型
 *            <p>
 *            Created by Yomii on 2016/6/14.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder<T>> {

    protected List<T> dataList;

    public BaseRecyclerAdapter() {
        this(null);
    }

    public BaseRecyclerAdapter(List<T> dataList) {
        this.dataList = (dataList == null ? new ArrayList<T>() : dataList);
        setHasStableIds(true);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        if (dataList != null) {
            this.dataList = dataList;
            notifyDataSetChanged();
        }
    }

    public void addDataFromList(List<T> dataList) {
        if (dataList != null) {
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void addData(T data) {
        if (data != null) {
            this.dataList.add(data);
            notifyDataSetChanged();
        }
    }

    public void addData(int position, T data) {
        if (data != null && position > -1 && (position < dataList.size() || position == 0)) {
            this.dataList.add(position, data);
            notifyDataSetChanged();
        }
    }

    public void removeData(T data) {
        if (dataList.contains(data)) {
            this.dataList.remove(data);
            notifyDataSetChanged();
        }
    }

    public void removeData(int position) {
        if (position < dataList.size() && position > -1) {
            dataList.remove(position);
            notifyItemRemoved(position);
            if (position == 0)
                notifyItemChanged(position);
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


    @Override
    public void onBindViewHolder(BaseRecyclerHolder<T> holder, int position) {
        holder.setData(dataList.get(position), position);
    }

}