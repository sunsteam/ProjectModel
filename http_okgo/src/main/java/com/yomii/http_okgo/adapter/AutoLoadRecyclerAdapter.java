package com.yomii.http_okgo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yomii.base.BaseRecyclerHolder;
import com.yomii.base.bean.RequestBean;
import com.yomii.http_okgo.R;

import java.lang.reflect.Type;


/**
 * Created by Yomii on 2017/6/16.
 *
 * @param <D>
 */

public abstract class AutoLoadRecyclerAdapter<D> extends WebRecyclerAdapter<D, RecyclerView.ViewHolder> {

    public static final int ITEM_LOAD_MORE = -3;

    public AutoLoadRecyclerAdapter(RequestBean request, Type responseType) {
        super(request, responseType);
    }

    public D getItem(int position) {
        if (position > dataList.size() - 1 || position < 0) {
            return null;
        }
        return dataList.get(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()) {
            return ITEM_LOAD_MORE;
        }
        return getViewTypeId(position);
    }

    /**
     * 如果内容是复杂布局, 有多类型的itemView,重写此方法
     *
     * @return 根据不同情况返回item类型
     */
    protected int getViewTypeId(int position) {
        return 0;
    }

    //--------------------------------更新底布局--------------------------------------//

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_LOAD_MORE)
            return new LoadMoreVH(parent, 0);
        else
            return getViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount()) {
            LoadMoreVH loadMoreVH = (LoadMoreVH) holder;
            if (state == STATE_HASMORE)
                load(); //获取下一页

            loadMoreVH.setData(state, position);
        } else {
            BaseRecyclerHolder<D> realVH = (BaseRecyclerHolder<D>) holder;
            realVH.setData(dataList.get(position), position);
        }

    }

    /**
     * 获取条目控制器
     *
     * @param parent   父控件
     * @param viewType 条目类型
     *
     * @return BaseViewHolder
     */
    protected abstract BaseRecyclerHolder<D> getViewHolder(ViewGroup parent, int viewType);


    private class LoadMoreVH extends BaseRecyclerHolder<Integer> {

        LoadMoreVH(ViewGroup parent, @LayoutRes int layoutRes) {
            super(parent, layoutRes);
        }

        private ProgressBar mProgressBar;
        private TextView mTextView;


        @Override
        protected void initView(View itemView) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setLayoutParams(new AbsListView.LayoutParams(-1, -2));

            mProgressBar = new ProgressBar(getContext());

            mTextView = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
            layoutParams.leftMargin = margin;
            layoutParams.rightMargin = margin;
            mTextView.setLayoutParams(layoutParams);
            mTextView.setText("加载中...");
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            linearLayout.addView(mProgressBar);
            linearLayout.addView(mTextView);

            ViewGroup parent = (ViewGroup) itemView;
            parent.addView(linearLayout);
        }

        @Override
        protected void setDataToView(Integer data, int position) {
            switch (data) {
                case STATE_EMPTY:
                    setInfo(false, R.string.load_empty);
                    break;
                case STATE_ERROR:
                    setInfo(false, R.string.load_error);
                    break;
                case STATE_DATASOURCEERROR:
                    setInfo(false, R.string.list_server_error);
                    break;
                case STATE_LOADING:
                    setInfo(true, R.string.load_more);
                    break;
                case STATE_NOMORE:
                    setInfo(false, R.string.load_nomore);
                    break;
            }
        }

        private void setInfo(boolean showProgress, @StringRes int info) {
            mProgressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
            mTextView.setText(info);
        }
    }


}
