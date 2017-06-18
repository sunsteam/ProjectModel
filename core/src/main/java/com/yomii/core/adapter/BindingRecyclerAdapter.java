package com.yomii.core.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yomii.base.BaseRecyclerAdapter;


/**
 * Created by Yomii on 2017/6/18.
 * <p>
 * 有一个通用VH， 快速创建使用DataBinding的适配器
 */

public abstract class BindingRecyclerAdapter<D> extends BaseRecyclerAdapter<D, BindingRecyclerAdapter.VH> {

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return VH.createVH(parent, getLayoutRes(viewType), getBRId(viewType));
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindData(dataList.get(position));
    }

    /**
     * @param viewType viewType
     *
     * @return Binding LayoutRes
     */
    protected abstract
    @LayoutRes
    int getLayoutRes(int viewType);

    /**
     * @param viewType viewType
     *
     * @return id in BR file
     */
    protected abstract int getBRId(int viewType);


    static class VH extends RecyclerView.ViewHolder {

        private int brId;
        private ViewDataBinding bind;

        private VH(View itemView, int brId) {
            super(itemView);
            this.brId = brId;
            bind = DataBindingUtil.bind(itemView);
        }

        static VH createVH(ViewGroup parent, @LayoutRes int layoutRes, int brId) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
            return new VH(itemView, brId);
        }

        void bindData(Object data) {
            bind.setVariable(brId, data);
            bind.executePendingBindings();//立即刷新绑定数据的更改
        }

    }
}
