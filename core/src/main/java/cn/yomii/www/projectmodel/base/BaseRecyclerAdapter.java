package cn.yomii.www.projectmodel.base;


import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * RecyclerView 基类
 *
 * @param <D> 数据包装类型
 *            <p>
 *            Created by Yomii on 2016/6/14.
 */
public abstract class BaseRecyclerAdapter<D>
        extends RecyclerView.Adapter<BaseRecyclerHolder<D>>
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


    @Override
    public void onBindViewHolder(BaseRecyclerHolder<D> holder, int position) {
        holder.setData(dataList.get(position), position);
    }


    /*________________________________________分页请求部分_________________________________________*/


    /**
     * 数据状态
     */
    protected int state = STATE_HASMORE;

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
     */
    public void onFilterSuccess(List<D> data) {
        if (data != null && data.size() > 0) {
            state = data.size() < pageSize ? STATE_NOMORE : STATE_HASMORE;
            //List增加数据
            addDataFromList(data);
        } else {
            state = dataList.size() > 0 ? STATE_NOMORE : STATE_EMPTY;
            notifyDataSetChanged();
        }
    }

    /**
     * 根据不同的错误更新数据状态
     *
     * @param state state
     */
    public void onErrorState(@State int state) {
        this.state = state;
        notifyDataSetChanged();
    }

    /**
     * @return 当前数据状态
     */
    @State
    public int getState() {
        return state;
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