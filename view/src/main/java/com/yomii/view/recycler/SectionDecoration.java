package com.yomii.view.recycler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Yomii on 2017/8/3.
 * <p>
 * 实现分组效果的Decoration
 */

public class SectionDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "SectionDecoration";
    private DecorationCallback callback;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private Paint.FontMetrics fm;

    public SectionDecoration(Context context, DecorationCallback decorationCallback) {
        Resources res = context.getResources();
        this.callback = decorationCallback;
        paint = new Paint();
        fm = new Paint.FontMetrics();
        textPaint = new TextPaint();
        paint.setColor(Color.parseColor("#FF4081"));
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(80);
        textPaint.setColor(Color.BLACK);
        textPaint.getFontMetrics(fm);
        textPaint.setTextAlign(Paint.Align.LEFT);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        topGap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);//32dp
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        Log.i(TAG, "getItemOffsets：" + pos);
        long groupId = callback.getGroupId(pos);
        if (groupId < 0)
            return;
        if (pos == 0 || isFirstInGroup(pos)) {//同组的第一个才添加padding
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            long groupId = callback.getGroupId(position);
            if (groupId < 0)
                return;
            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (position == 0 || isFirstInGroup(position)) {
                float top = view.getTop() - topGap;
                float bottom = view.getTop();
                int baseline = (int) (bottom - topGap / 2 + (fm.descent - fm.ascent) / 2 - fm.descent);
                c.drawRect(left, top, right, bottom, paint);//绘制红色矩形
                c.drawText(textLine, left, baseline, textPaint);//绘制文本
            }
        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            long prevGroupId = callback.getGroupId(pos - 1);
            long groupId = callback.getGroupId(pos);
            return prevGroupId != groupId;
        }
    }

}
