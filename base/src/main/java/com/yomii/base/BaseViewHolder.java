package com.yomii.base;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;


/**
 * ListView 的 ViewHolder 基类
 *
 * @param <D> 条目对应的数据类型
 *
 * @author Yomii
 */
public abstract class BaseViewHolder<D> {

    private View mView;
    private D data;
    private int position;
    private Context context;

    public BaseViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        this.context = parent.getContext();
        mView = initView(context, parent, layoutRes);
        if (mView == null) {
            throw new RuntimeException("None from initView");
        }
        mView.setTag(this);
    }

    public void setData(D data, int position) {
        this.data = data;
        this.position = position;
        if (data != null) {
            setDataToView(data, position);
        }
    }

    public View getRootView() {
        return mView;
    }

    public D getData() {
        return data;
    }

    public int getPosition() {
        return position;
    }

    public Context getContext() {
        return context;
    }

    /**
     * 把条目对应的xml布局转换成View对象
     *
     * @return rootview
     */
    protected abstract View initView(Context context, ViewGroup parent, int layoutRes);

    /**
     * 把条目对应的数据设置到控件上
     */
    protected abstract void setDataToView(D data, int position);
}
