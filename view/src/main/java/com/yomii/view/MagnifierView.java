package com.yomii.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.view.View;

/**
 * 放大镜控件, 填充一个 Bitmap, 放大 n 倍, 并进行一个圆形裁切
 * Created by Yomii on 2016/4/5.
 */
public class MagnifierView extends View {

    private Path mPath = new Path();
    private Matrix matrix = new Matrix();
    private Bitmap bitmap;
    //放大镜的半径
    public static final int RADIUS = 160;
    //放大倍数
    private static final int FACTOR = 2;
    private int mCurrentX, mCurrentY;

    public MagnifierView(Context context, Bitmap bitmap) {
        super(context);
        mPath.addCircle(RADIUS, RADIUS, RADIUS, Path.Direction.CW);
        matrix.setScale(FACTOR, FACTOR);
        this.bitmap = bitmap;
        invalidate();
    }

    public void setBitmap(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        mCurrentX = x;
        mCurrentY = y;
        invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //底图
        //canvas.drawBitmap(bitmap, 0, 0, null);
        //剪切
        //canvas.translate(mCurrentX-RADIUS, mCurrentY-RADIUS);
        //canvas.translate(RADIUS-mCurrentX*FACTOR, RADIUS-mCurrentY*FACTOR);
        canvas.drawBitmap(bitmap, matrix, null);
        canvas.clipPath(mPath);
    }
}
