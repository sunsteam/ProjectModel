package cn.yomii.www.frame.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.List;

import cn.yomii.www.frame.R;


/**
 * 雷达图
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

    private int cornerCount;
    private int strokeColor = 0xFFc9c9c9;
    private int strokeWidth;
    private int textPadding;

    private float centerX;
    private float centerY;
    private PointF centerPoint;
    private float radius;

    private float textWidth;
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
        //初始化颜色和尺寸信息
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics);
        textPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
        int sp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, displayMetrics);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        int textSize = a.getDimensionPixelSize(0, sp);
        int textColor = a.getColor(1, 0xFF333333);

        a.recycle();
        //获取自定义属性
        a = context.obtainStyledAttributes(attrs, R.styleable.Radar);

        strokeColor = a.getColor(R.styleable.Radar_strokeColor, strokeColor);
        strokeWidth = a.getDimensionPixelOffset(R.styleable.Radar_strokeWidth, strokeWidth);
        textPadding = a.getDimensionPixelOffset(R.styleable.Radar_textPadding, textPadding);

        a.recycle();
        //初始化形状和文字画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
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
                //要求页面加载完毕前设置好参数数量(顶点数量),参数文字并测量文字宽高,初始化顶点容器
                if (argumentList == null || argumentList.size() < 3)
                    throw new IllegalArgumentException("setArgumentList必须在布局加载前调用");
                //获取控件中心点
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
                centerPoint = new PointF(centerX, centerY);
                //根据宽高、padding、文字的宽高计算半径,取宽高最小值/2
                radius = Math.min(
                        getWidth() - getPaddingLeft() - getPaddingRight() - (textWidth + textPadding) * 2,
                        getHeight() - getPaddingTop() - getPaddingBottom() - (textHeight + textPadding / 2) * 2
                ) / 2;
                //每个顶点之间的间隔角度
                float angle = 360 / cornerCount;
                //因为path初始绘制点在X轴正方向,而不是Y轴正方向,因此初始点需要偏转90度 +第一条路径的对应角度
                float startAngle = -90 - angle;
                //设置图形范围
                RectF area = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
                PathMeasure pathMeasure = new PathMeasure();
                float[] floats = new float[2];
                //根据顶点数量,测量每个顶点的位置,并设置到顶点容器
                for (int i = 0; i < cornerCount; i++) {
                    path.reset();
                    //只有半圆形能设置初始角度,圆形不可以
                    path.addArc(area, startAngle, angle);
                    startAngle += angle;
                    pathMeasure.setPath(path, false);
                    //根据路径长度,测量顶点坐标
                    pathMeasure.getPosTan(pathMeasure.getLength(), floats, null);
                    points[i] = new PointF();
                    points[i].x = (int) floats[0];
                    points[i].y = (int) floats[1];
                }
            }
        });

    }


    public List<Argument> getArgumentList() {
        return argumentList;
    }

    /**
     * 设置参数列表并初始化图形
     *
     * @param argumentList 参数列表
     */
    public void setArgumentList(List<Argument> argumentList) {
        if (argumentList == null || argumentList.size() < 3)
            throw new IllegalArgumentException("参数数量不可小于3个");
        this.argumentList = argumentList;
        //顶点数量
        cornerCount = argumentList.size();
        //图层顶点容器
        layers = new PointF[cornerCount];
        //边框顶点容器
        points = new PointF[cornerCount];
        //测量参数文字宽高
        measureText();
    }

    public SparseArray<float[]> getLayerList() {
        return layerList;
    }

    /**
     * 设置图层信息并绘制图形
     *
     * @param layerList index: 图层颜色 ; <p> float[]: 每个参数的实际值的数组
     */
    public void setLayerList(SparseArray<float[]> layerList) {
        this.layerList = layerList;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);
        //画多边形轮廓
        drawPath(canvas, paint, points);
        //画中间点和顶点间的连线
        for (PointF point : points) {
            canvas.drawLine(centerPoint.x, centerPoint.y, point.x, point.y, paint);
        }

        if (layerList != null && layerList.size() > 0) {
            int size = layerList.size();
            //重设画笔
            paint.setStyle(Paint.Style.FILL);
            //根据数据层数量,设置不同颜色
            for (int i = 0; i < size; i++) {
                paint.setColor(layerList.keyAt(i));

                float[] value = layerList.valueAt(i);

                for (int j = 0; j < cornerCount; j++) {
                    //根据参数实际值/最大值的分度,计算中心点到顶点之间的对应点距离
                    float percent = value[j] / argumentList.get(j).argumentValue;
                    layers[j] = getPointByPercent(centerPoint, points[j], percent);
                }
                //画内层
                drawPath(canvas, paint, layers);
            }
        }
        //画文字
        for (int i = 0; i < cornerCount; i++) {
            float offsetX;
            float offsetY;
            //根据点的横坐标和中心点横坐标的对应关系, 设置文字起始点横坐标
            if (points[i].x <= centerX) {
                if (Math.abs(centerX - points[i].x) <= 1)
                    offsetX = points[i].x - (textWidth / 2);
                else
                    offsetX = points[i].x - textWidth - textPadding;
            } else {
                offsetX = points[i].x + textPadding;
            }
            //设置文字起始点纵坐标
            if (points[i].y <= centerY) {
                if (Math.abs(centerX - points[i].x) <= 1)
                    offsetY = points[i].y - textPadding / 2;
                else
                    offsetY = points[i].y + textPadding / 2;
            } else {
                offsetY = points[i].y + textHeight;
            }

            canvas.drawText(argumentList.get(i).argumentName, offsetX, offsetY, textPaint);
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
            //宽度取所有文字中的最大值
            float width = textPaint.measureText(argument.argumentName);
            textWidth = textWidth < width ? width : textWidth;
        }
    }

    /**
     * 图形参数
     */
    public static class Argument {

        /**
         * 参数名称
         */
        private String argumentName;

        /**
         * 参数最大值
         */
        private float argumentValue;


        public Argument(String argumentName, float argumentValue) {
            this.argumentName = argumentName;
            this.argumentValue = argumentValue;
        }
    }

    /**
     * Get point between p1 and p2 by percent. 根据百分比获取两点之间的某个点坐标
     */
    public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {
        return new PointF(evaluateValue(percent, p1.x, p2.x), evaluateValue(percent, p1.y, p2.y));
    }

    /**
     * 根据分度值，计算从start到end中，fraction位置的值。fraction范围为0 -> 1
     */
    public static float evaluateValue(float fraction, Number start, Number end) {
        return start.floatValue() + (end.floatValue() - start.floatValue()) * fraction;
    }

}
