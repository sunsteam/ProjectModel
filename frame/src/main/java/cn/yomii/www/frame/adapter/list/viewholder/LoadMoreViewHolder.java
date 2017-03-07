package cn.yomii.www.frame.adapter.list.viewholder;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.yomii.www.frame.R;
import cn.yomii.www.frame.adapter.Loader;


public class LoadMoreViewHolder extends BaseViewHolder<Integer> {

    public LoadMoreViewHolder(ViewGroup parent) {
        super(parent);
    }

    private ProgressBar mProgressBar;
    private TextView mTextView;

    @Override
    public View initView(Context context, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.common_load_more, parent, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mTextView = (TextView) view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void setDataToView(Integer data, int position) {
        switch (data) {
            case Loader.STATE_EMPTY:
                setInfo(false, R.string.load_empty);
                break;
            case Loader.STATE_ERROR:
                setInfo(false, R.string.load_error);
                break;
            case Loader.STATE_DATASOURCEERROR:
                setInfo(false, R.string.list_server_error);
                break;
            case Loader.STATE_LOADING:
                setInfo(true, R.string.load_more);
                break;
            case Loader.STATE_NOMORE:
                setInfo(false, R.string.load_nomore);
                break;
        }
    }

    private void setInfo(boolean showProgress, @StringRes int info) {
        mProgressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
        mTextView.setText(info);
    }

}
