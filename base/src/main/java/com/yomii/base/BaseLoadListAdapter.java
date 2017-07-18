package com.yomii.base;

import com.yomii.base.bean.ListResponse;

import java.util.List;

/**
 * Created by Yomii on 2017/7/18.
 * <p>
 * 数据读取Adapter基类，提供数据读取各阶段的调用实现，子类需要处理读取和回调
 */

public abstract class BaseLoadListAdapter<D> extends BaseListAdapter<D> implements LoaderState {

    private OnLoadAspectListener listener;

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
     * 是否已通知视图
     */
    protected boolean alreadyNotify;


    /**
     * 设置读取数据前后在View上的回调
     *
     * @param listener 读取状态切面的监听器
     */
    public void setListener(OnLoadAspectListener listener) {
        this.listener = listener;
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

    /**
     * 对外暴露的load方法, 更改状态为加载状态, 子类应复写onLoad处理数据读取
     */
    public final void load() {
        setState(STATE_LOADING);
        onLoad();
    }

    /**
     * 重设数据状态
     */
    public void reset(){
        setState(STATE_HASMORE);
        setIndex(0);
        clearDataList();
    }


    protected void onLoadBefore() {
        alreadyNotify = false;

        if (listener != null) {
            listener.onLoadBefore(state, index, pageSize);
        }
    }

    protected void onLoadSuccess(ListResponse<D> resp) {
        if (onDataFilter(resp)) {
            //更新页数
            index = resp.getIndex();
            alreadyNotify = onFilteredSuccess(resp.getData());
        }
    }


    /**
     * @param data 新增数据 根据返回的data内容更新数据列表
     *
     * @return 是否已经 notify adapter change ， true: 是 ； false： 否
     */
    protected boolean onFilteredSuccess(List<D> data) {
        if (data != null && data.size() > 0) {
            state = data.size() < pageSize ? STATE_NOMORE : STATE_HASMORE;
            //List增加数据
            addDataFromList(data);
            return true;
        } else {
            state = dataList.size() > 0 ? STATE_NOMORE : STATE_EMPTY;
            return false;
        }
    }

    protected void onLoadError() {
        setState(STATE_ERROR);
    }

    protected void onLoadException() {
        setState(STATE_DATASOURCEERROR);
    }

    protected void onLoadAfter() {
        if (!alreadyNotify)
            notifyDataSetInvalidated();

        if (listener != null) {
            listener.onLoadAfter(state);
        }
    }

    /**
     * 子类可通过复写此方法过滤数据, 默认不过滤
     *
     * @param response 返回的结果封装
     *
     * @return 如果数据符合条件，则返回true，否则false
     */
    protected boolean onDataFilter(ListResponse<D> response) {
        return true;
    }

    /**
     * 实现以获取数据
     */
    protected abstract void onLoad();


}
