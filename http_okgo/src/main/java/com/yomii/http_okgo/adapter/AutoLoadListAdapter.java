package com.yomii.http_okgo.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yomii.base.BaseViewHolder;
import com.yomii.base.bean.RequestBean;
import com.yomii.http_okgo.R;

import java.lang.reflect.Type;


/**
 * Created by Yomii on 2017/1/12.
 * <p>
 * 滑动到底部自动加载下一页的ListView Adapter
 * <p>
 * 注意: onItemClickListener 中取得的position 比 dataList 中多1, 应注意判空和数组越界
 */
public abstract class AutoLoadListAdapter<D> extends WebListAdapter<D> {

    public AutoLoadListAdapter(RequestBean request, Type responseType) {
        super(request, responseType);
    }

    @Override
    public D getItem(int position) {
        if (position > dataList.size() - 1 || position < 0) {
            return null;
        }
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size() + 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == dataList.size()) {
            return 1;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        //最后一条是底布局
        LoadMoreViewHolder mFootViewHolder;
        if (position == getCount() - 1) {
            if (convertView == null) {
                mFootViewHolder = new LoadMoreViewHolder(parent);
            } else {
                mFootViewHolder = (LoadMoreViewHolder) convertView.getTag();
            }
            if (state == STATE_HASMORE)
                load(); //获取下一页

            mFootViewHolder.setData(state, position);

            return mFootViewHolder.getRootView();
        }

        return super.getView(position, convertView, parent);
    }


    private class LoadMoreViewHolder extends BaseViewHolder<Integer> {

        public LoadMoreViewHolder(ViewGroup parent) {
            super(parent, 0);
        }

        private ProgressBar mProgressBar;
        private TextView mTextView;

        @Override
        protected View initView(Context context, ViewGroup parent, int layoutRes) {
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
            return linearLayout;
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
