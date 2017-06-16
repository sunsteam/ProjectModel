package cn.yomii.www.projectmodel.adapter;

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

import cn.yomii.www.projectmodel.R;
import cn.yomii.www.projectmodel.base.BaseViewHolder;
import cn.yomii.www.projectmodel.base.LoaderState;


public class LoadMoreViewHolder extends BaseViewHolder<Integer> {

    public LoadMoreViewHolder(ViewGroup parent) {
        super(parent,0);
    }

    private ProgressBar mProgressBar;
    private TextView mTextView;

    @Override
    public View initView(Context context, ViewGroup parent, int layoutRes) {
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
    public void setDataToView(Integer data, int position) {
        switch (data) {
            case LoaderState.STATE_EMPTY:
                setInfo(false, R.string.load_empty);
                break;
            case LoaderState.STATE_ERROR:
                setInfo(false, R.string.load_error);
                break;
            case LoaderState.STATE_DATASOURCEERROR:
                setInfo(false, R.string.list_server_error);
                break;
            case LoaderState.STATE_LOADING:
                setInfo(true, R.string.load_more);
                break;
            case LoaderState.STATE_NOMORE:
                setInfo(false, R.string.load_nomore);
                break;
        }
    }

    private void setInfo(boolean showProgress, @StringRes int info) {
        mProgressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
        mTextView.setText(info);
    }

}
