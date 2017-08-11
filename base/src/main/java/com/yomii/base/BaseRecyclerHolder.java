package com.yomii.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * RecyclerView 的 ViewHolder 基类
 *
 * @param <D> 条目对应的数据类型
 *
 * @author Yomii
 */
public abstract class BaseRecyclerHolder<D> extends RecyclerView.ViewHolder {

    private D data;
    private Context context;
    private SparseArray<View> views;

    public BaseRecyclerHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(generateItemView(parent, layoutRes));
        views = new SparseArray<>();
        this.context = parent.getContext();
        initView(itemView);
    }

    private static View generateItemView(ViewGroup parent, @LayoutRes int layoutRes) {
        if (layoutRes != 0)
            return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        else
            return new FrameLayout(parent.getContext());
    }


    public D getData() {
        return data;
    }

    protected Context getContext() {
        return context;
    }

    public void setData(D data, int position) {
        this.data = data;
        if (data != null) {
            setDataToView(data, position);
        }
    }


    @SuppressWarnings("unchecked")
    public <V extends View> V getView(@IdRes int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (V) view;
    }

    public void setText(@IdRes int id, String textContent) {
        TextView tv = getView(id);
        tv.setText(textContent);
    }



    /**
     * 把条目对应的数据设置到控件上
     */
    protected abstract void setDataToView(D data, int position);

    /**
     * 布局对象的绑定和初始化
     */
    protected abstract void initView(View itemView);


}