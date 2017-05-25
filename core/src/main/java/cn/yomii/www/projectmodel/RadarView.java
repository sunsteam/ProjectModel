package cn.yomii.www.projectmodel;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.List;

import cn.yomii.www.projectmodel.utils.GeometryUtil;

/**
 * Created by Yomii on 2017/5/25.
 */

public class RadarView extends View {

    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };

    private TextPaint textPaint;
    private Paint paint;
    private Path path;
    private PointF[] points;
    private PointF[] layers;

    private int cornerCount = 5;
    private int strokeColor = 0xFF7c818a;
    private int strokeWidth;

    private float centerX;
    private float centerY;
    private PointF centerPoint;
    private float radius;

    private float textLength;
    private float textHeight;


    private List<Argument> argumentList;
    private SparseArray<float[]> layerList;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        int textSize = a.getDimensionPixelSize(0, 22);
        int textColor = a.getColor(1, 0xFF333333);

        a.recycle();

        a = context.obtainStyledAttributes(attrs, R.styleable.Radar);

        strokeColor = a.getColor(R.styleable.Radar_strokeColor, strokeColor);
        strokeWidth = a.getDimensionPixelOffset(R.styleable.Radar_strokeColor, strokeWidth);

        a.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        Paint.FontMetrics fm = new Paint.FontMetrics();
        textPaint.getFontMetrics(fm);
        textHeight = fm.bottom - fm.top;

        path = new Path();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (argumentList == null || argumentList.size() < 3)
                    throw new IllegalArgumentException("setArgumentList必须在布局加载前调用");

                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
                centerPoint = new PointF(centerX, centerY);

                radius = Math.min(
                        getWidth() - getPaddingLeft() - getPaddingRight() - textLength * 2,
                        getHeight() - getPaddingTop() - getPaddingBottom() - textHeight * 2
                ) /2;
                path.reset();
                path.moveTo(centerX, centerY + radius);
                path.addCircle(centerX, centerY, radius, Path.Direction.CW);

                PathMeasure pathMeasure = new PathMeasure(path, false);

                float[] floats = new float[2];
                float segment = pathMeasure.getLength() / cornerCount;
                for (int i = 0; i < cornerCount; i++) {
                    points[i] = new PointF();
                    pathMeasure.getPosTan(i * segment, floats, null);
                    points[i].x = (int) floats[0];
                    points[i].y = (int) floats[1];
                }
            }
        });

    }


    public List<Argument> getArgumentList() {
        return argumentList;
    }

    public void setArgumentList(List<Argument> argumentList) {
        if (argumentList == null || argumentList.size() < 3)
            throw new IllegalArgumentException("参数数量不可小于3个");
        this.argumentList = argumentList;
        cornerCount = argumentList.size();
        layers = new PointF[cornerCount];
        points = new PointF[cornerCount];
        measureText();
    }

    public SparseArray<float[]> getLayerList() {
        return layerList;
    }

    public void setLayerList(SparseArray<float[]> layerList) {
        this.layerList = layerList;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(-90, centerX, centerY);
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);
        drawPath(canvas, paint, points);

        for (PointF point : points) {
            canvas.drawLine(centerPoint.x, centerPoint.y, point.x, point.y, paint);
        }

        if (layerList != null && layerList.size() > 0) {
            int size = layerList.size();
            paint.reset();
            paint.setStyle(Paint.Style.FILL);

            for (int i = 0; i < size; i++) {
                paint.setColor(layerList.keyAt(i));

                float[] value = layerList.valueAt(i);

                for (int j = 0; j < cornerCount; j++) {
                    float percent = value[j] / argumentList.get(j).argumentValue;
                    layers[j] = GeometryUtil.getPointByPercent(centerPoint, points[j], percent);
                }
                drawPath(canvas, paint, layers);
            }
        }
        for (int i = 0; i < cornerCount; i++) {
            canvas.drawText(argumentList.get(i).argumentName,points[i].x,points[i].y,textPaint);
        }
    }

    private void drawPath(Canvas canvas, Paint paint, PointF[] points) {
        path.reset();
        for (int i = 0; i < cornerCount; i++) {
            if (i == 0)
                path.moveTo(points[i].x, points[i].y);
            else
                path.lineTo(points[i].x, points[i].y);
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    private void measureText() {
        for (Argument argument : argumentList) {
            float length = textPaint.measureText(argument.argumentName);
            textLength = textLength < length ? length : textLength;
        }
    }

    public static class Argument {

        private String argumentName;

        private float argumentValue;


        public Argument(String argumentName, float argumentValue) {
            this.argumentName = argumentName;
            this.argumentValue = argumentValue;
        }
    }

}
