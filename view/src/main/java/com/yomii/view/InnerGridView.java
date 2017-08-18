package com.yomii.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Yomii on 2016/8/3.
 * <p>
 * 放置于滑动布局中的GridView
 */
public class InnerGridView extends GridView {

    public InnerGridView(Context context) {
        super(context);
    }

    public InnerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
