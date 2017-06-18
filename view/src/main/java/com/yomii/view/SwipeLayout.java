package com.yomii.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 可滑动布局, 只可从xml设置
 * <p>
 * 第一个布局为button 第二个布局为content
 * <p>
 * Created by Yomii on 2016/4/16.
 */
public class SwipeLayout extends FrameLayout {

    public interface OnStateChangeListener {
        void onOpen(View child0, View child1);

        void onClose(View child0, View child1);

        void onCaptured(View child0, View child1);
    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    private View mButtons;
    private View mContents;
    private int mRange;
    private int mHeight;
    private int mContentsWidth;
    private Rect mRect;
    private ViewDragHelper mDrager;

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mRect = new Rect();
        mDrager = ViewDragHelper.create(this, mCallback);
    }

    ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //位置限定
            if (child == mContents) {
                left = Math.min(left, 0);
                left = Math.max(left, -mRange);
            } else if (child == mButtons) {
                left = Math.min(left, mContentsWidth);
                left = Math.max(left, mContentsWidth - mRange);
            }

            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mContents) {
                mButtons.offsetLeftAndRight(dx);
            } else if (changedView == mButtons) {
                mContents.offsetLeftAndRight(dx);
            }
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == mContents || releasedChild == mButtons) {
                if (mContents.getLeft() > (-mRange / 2)) {
                    close();
                } else {
                    open();
                }
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:
                    if (onStateChangeListener != null) {
                        onStateChangeListener.onCaptured(mButtons, mContents);
                    }
                    break;
                case ViewDragHelper.STATE_IDLE:
                    if (mContents.getLeft() == 0) {
                        if (onStateChangeListener != null) {
                            onStateChangeListener.onClose(mButtons, mContents);
                        }
                    } else {
                        if (onStateChangeListener != null) {
                            onStateChangeListener.onOpen(mButtons, mContents);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mButtons = getChildAt(0);
        mContents = getChildAt(1);
    }

    public void open() {
        open(true);

    }

    private void open(boolean isSmooth) {
        if (isSmooth) {
            int finalLeft = -mRange;
            mDrager.smoothSlideViewTo(mContents, finalLeft, 0);
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            layoutChildren(true);
        }
    }

    public void close() {
        close(true);
    }

    private void close(boolean isSmooth) {
        if (isSmooth) {
            int finalLeft = 0;
            mDrager.smoothSlideViewTo(mContents, finalLeft, 0);
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            layoutChildren(false);
        }
    }

    @Override
    public void computeScroll() {
        if (mDrager.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        mRange = mButtons.getMeasuredWidth();
        mHeight = mContents.getMeasuredHeight();
        mContentsWidth = mContents.getMeasuredWidth();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //mButtons.layout(mContentsWidth, 0, mContentsWidth+mRange, mHeight);
        layoutChildren(false);
    }

    private void layoutChildren(boolean isOpen) {
        Rect contentsRect = computeContentRect(isOpen);
        mContents.layout(contentsRect.left, contentsRect.top, contentsRect.right, contentsRect.bottom);
        Rect buttonsRect = computeButtonsRect();
        mButtons.layout(buttonsRect.left, buttonsRect.top, buttonsRect.right, buttonsRect.bottom);
    }

    private Rect computeContentRect(boolean isOpen) {

        int left = isOpen ? -mRange : 0;

        mRect.set(left, 0, left + mContentsWidth, mHeight);
        return mRect;
    }

    /**
     * 通过mContent的位置计算mButton的位置, 因此只能在computeContentRect之后调用
     *
     * @return 按钮的位置封装
     */
    private Rect computeButtonsRect() {

        int left = mRect.right;

        mRect.set(left, 0, left + mRange, mHeight);

        return mRect;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        mDrager.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDrager.shouldInterceptTouchEvent(ev);
    }


}
