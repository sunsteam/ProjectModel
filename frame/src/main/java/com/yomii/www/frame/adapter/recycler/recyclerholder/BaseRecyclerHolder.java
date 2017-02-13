package com.yomii.www.frame.adapter.recycler.recyclerholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * RecyclerView 的 ViewHolder 基类
 *
 * @param <K> 条目对应的数据类型
 *
 * @author Yomii
 */
public abstract class BaseRecyclerHolder<K> extends RecyclerView.ViewHolder {

    public BaseRecyclerHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
        this.context = parent.getContext();
        initView(itemView);
    }

    protected K data;
    protected Context context;

    public K getData() {
        return data;
    }

    public void setData(K data, int position) {
        this.data = data;
        if (data != null) {
            setDataToView(data, position);
        }
    }

    /**
     * 把条目对应的数据设置到控件上
     */
    protected abstract void setDataToView(K data, int position);

    /**
     * 布局对象的绑定和初始化
     */
    protected abstract void initView(View itemView);


}