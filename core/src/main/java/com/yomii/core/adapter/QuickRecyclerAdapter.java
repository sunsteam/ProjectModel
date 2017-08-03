package com.yomii.core.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yomii.base.BaseRecyclerAdapter;


/**
 * Created by Yomii on 2017/6/18.
 * <p>
 * 有一个通用VH， 快速创建只需要展示数据的简单适配器
 */

public abstract class QuickRecyclerAdapter<D> extends BaseRecyclerAdapter<D, QuickRecyclerAdapter.VH> {

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return VH.createVH(parent, getLayoutRes(viewType));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        convertView(holder, dataList.get(position), position);
    }

    /**
     * @param viewType viewType
     *
     * @return if 0 ， 创建一个空的FrameLayout作为View， 否则根据返回的layoutId创建View
     */
    protected abstract int getLayoutRes(int viewType);

    /**
     * 填充数据
     *
     * @param holder   holder
     * @param d        data
     * @param position position
     */
    protected abstract void convertView(VH holder, D d, int position);


    protected static class VH extends RecyclerView.ViewHolder {

        private SparseArray<View> views;

        private VH(View itemView) {
            super(itemView);
            views = new SparseArray<>();
        }

        static VH createVH(ViewGroup parent, @LayoutRes int layoutRes) {
            View itemView;
            if (layoutRes != 0)
                itemView = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
            else
                itemView = new FrameLayout(parent.getContext());

            return new VH(itemView);
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
    }
}
