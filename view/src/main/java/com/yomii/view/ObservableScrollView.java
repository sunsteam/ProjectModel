package com.yomii.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 可获取滑动状态和滑动距离的ScrollView
 */
public class ObservableScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        this(context, null, 0);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId), 5);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!onMove && scrollViewListener != null) {
                            onMove = true;
                            scrollViewListener.onScrollStateChange(ObservableScrollView.this, 1);
                        }
                        break;
                }
                return false;
            }
        });
    }


    private int lastY = 0;
    private int touchEventId = -9983761;
    private boolean onMove;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == touchEventId) {
                if (lastY == getScrollY()) {
                    onMove = false;
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStateChange(ObservableScrollView.this, 0);
                    }
                } else {
                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId), 3);
                    lastY = getScrollY();
                }
            }
        }
    };

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }


    public interface ScrollViewListener {
        /**
         * 获取滑动增量, 可用于伴随动画等
         */
        void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                             int oldx, int oldy);

        /**
         * 获取滑动状态, 1: 开始滑动  0. 滑动完全停止
         */
        void onScrollStateChange(ObservableScrollView scrollView, int state);
    }

} 