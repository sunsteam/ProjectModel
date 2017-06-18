package com.yomii.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 放置于滑动布局中的ListView
 *
 * Created by Yomii on 2016/4/12.
 */
public class InnerListView extends ListView {
    public InnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerListView(Context context) {
        super(context);

    }

    public InnerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
