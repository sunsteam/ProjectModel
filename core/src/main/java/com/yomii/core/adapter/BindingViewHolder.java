package com.yomii.core.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yomii.base.BaseViewHolder;


/**
 * Created by Yomii on 2017/5/16.
 * <p>
 * BindingViewHolder
 */

public class BindingViewHolder<T> extends BaseViewHolder<T> {

    private int brId;
    private ViewDataBinding bind;

    public BindingViewHolder(ViewGroup parent, @LayoutRes int layoutRes, int brId) {
        super(parent, layoutRes);
        this.brId = brId;
    }

    @Override
    protected void setDataToView(T data, int i) {
        bind.setVariable(brId, data);
        bind.executePendingBindings();
    }

    @Override
    protected View initView(Context context, ViewGroup viewGroup, int layoutRes) {
        View itemView = LayoutInflater.from(context).inflate(layoutRes, viewGroup, false);
        bind = DataBindingUtil.bind(itemView);
        return itemView;
    }

}
