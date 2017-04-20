package cn.yomii.www.frame.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * List adapter 基类
 *
 * @param <T> 数据包装类型
 *            <p>
 *            Created by Yomii on 2016/6/14.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected List<T> dataList;


    public BaseListAdapter() {
        this(null);
    }

    public BaseListAdapter(List<T> dataList) {
        this.dataList = (dataList == null ? new ArrayList<T>() : dataList);
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

    public void removeData(int postion) {
        if (postion < dataList.size() && postion > -1) {
            this.dataList.remove(postion);
            notifyDataSetChanged();
        }
    }

    public void clearDataList() {
        dataList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //内容条目
        BaseViewHolder<T> holder;
        if (convertView == null) {
            holder = getViewHolder(position, parent);
        } else {
            holder = (BaseViewHolder<T>) convertView.getTag();
        }
        holder.setData(dataList.get(position), position);

        return holder.getRootView();
    }

    /**
     * 获取条目控制器
     *
     * @param position 位置
     * @param parent   父控件
     *
     * @return BaseViewHolder
     */
    protected abstract BaseViewHolder<T> getViewHolder(int position, ViewGroup parent);

}
