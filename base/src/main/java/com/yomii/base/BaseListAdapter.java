package com.yomii.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yomii on 2016/6/14.
 * <p>
 * List adapter 基类, 包含数据容器, 扩充数据加载方式, 封装分页请求所需要的元数据, 外部获取数据后可调用 onLoadSuccess
 * 或 setState 方法更新分页描述信息
 *
 * @param <D> data 数据包装类型
 */
public abstract class BaseListAdapter<D> extends BaseAdapter implements LoaderState {

    protected List<D> dataList;


    public BaseListAdapter() {
        this(null);
    }

    public BaseListAdapter(List<D> dataList) {
        this.dataList = (dataList == null ? new ArrayList<D>() : dataList);
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
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void addData(D data) {
        if (data != null) {
            this.dataList.add(data);
            notifyDataSetChanged();
        }
    }

    public void addData(int position, D data) {
        if (data != null && position > -1 && (position < dataList.size() || position == 0)) {
            this.dataList.add(position, data);
            notifyDataSetChanged();
        }
    }

    public void removeData(D data) {
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
    public D getItem(int position) {
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
        BaseViewHolder<D> holder;
        if (convertView == null) {
            holder = getViewHolder(position, parent);
        } else {
            holder = (BaseViewHolder<D>) convertView.getTag();
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
    protected abstract BaseViewHolder<D> getViewHolder(int position, ViewGroup parent);




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
