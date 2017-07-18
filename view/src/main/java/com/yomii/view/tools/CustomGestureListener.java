package com.yomii.view.tools;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 自定义左右滑动手势识别器,使用者需要接收GestureMoveEvent事件
 * Created by Yomii on 2016/3/21.
 */
public abstract class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {


    public CustomGestureListener(int triggerWidth) {
        this.triggerWidth = triggerWidth;
    }

    private int triggerWidth;

    boolean flag = true;
    float startX = 0;
    float startY = 0;
    float endX = 0;
    float endY = 0;

    @Override
    public boolean onDown(MotionEvent e) {
        flag = true;
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {

        if (flag) {
            if (e1 != null){
                startX = e1.getRawX();
                startY = e1.getRawY();
            }
            endX = e2.getRawX();
            endY = e2.getRawY();

            if (startX - endX > triggerWidth && ((Math.abs(startX - endX)) > (Math.abs(startY - endY)))) {
                flag = false;
                turnLeft();
                return true;
            }

            if (startX - endX < -triggerWidth && ((Math.abs(startX - endX)) > (Math
                    .abs(startY - endY)))) {
                flag = false;
                turnRight();
                return true;
            }
        }
        return false;
    }

    protected abstract void turnLeft();

    protected abstract void turnRight();
}
