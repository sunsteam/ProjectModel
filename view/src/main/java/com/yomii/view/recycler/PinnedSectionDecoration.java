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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Yomii on 2017/8/3.
 * <p>
 * 实现Sticky分组效果的Decoration
 */

public class PinnedSectionDecoration extends RecyclerView.ItemDecoration {

    private DecorationCallback callback;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private Paint.FontMetrics fm;

    public PinnedSectionDecoration(Context context, DecorationCallback decorationCallback) {
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
        long groupId = callback.getGroupId(pos);
        if (groupId < 0)
            return;
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        long preGroupId, groupId = -1;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            preGroupId = groupId;
            groupId = callback.getGroupId(position);
            if (groupId < 0 || groupId == preGroupId)
                continue;
            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.isEmpty(textLine))
                continue;
            int viewBottom = view.getBottom();
            float textY = Math.max(topGap, view.getTop());
            if (position + 1 < itemCount) { //下一个和当前不一样移动当前
                long nextGroupId = callback.getGroupId(position + 1);
                if (nextGroupId != groupId && viewBottom < textY) {//组内最后一个view进入了header
                    textY = viewBottom;
                }
            }
            int baseline = (int) (textY - topGap/2 + (fm.descent - fm.ascent)/2 - fm.descent);
            c.drawRect(left, textY - topGap, right, textY, paint);
            c.drawText(textLine, left, baseline, textPaint);
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
