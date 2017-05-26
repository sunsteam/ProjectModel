package cn.yomii.www.projectmodel;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 半圆形圆点进度条
 * Created by Yomii on 2017/5/26.
 */

public class ProgressView extends View {

    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };

    private TextPaint textPaint;
    private Paint paint;
    private Path path;

    private int strokeColor = 0xFF00b7ee;
    private int strokeWidth;
    private int dotPadding;
    private int headTextSize;
    private int headTextUnitSize;
    private int progressMax;
    private int progress;
    private String bottomText = "综合指数";
    private String headUnitText = "分";


    private float centerX;
    private float centerY;
    private float outRadius;
    private int percent;

    private float textHeight;
    private Bitmap indicatorBitmap;
    private int textSize;
    private int textColor;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, displayMetrics);
        dotPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics);
        int sp11 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, displayMetrics);
        int sp12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, displayMetrics);
        int sp5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, displayMetrics);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        textSize = a.getDimensionPixelSize(0, sp11);
        textColor = a.getColor(1, strokeColor);

        a.recycle();

        a = context.obtainStyledAttributes(attrs, R.styleable.DotProgress);

        strokeColor = a.getColor(R.styleable.DotProgress_strokeColor, strokeColor);
        strokeWidth = a.getDimensionPixelSize(R.styleable.DotProgress_strokeWidth, strokeWidth);
        dotPadding = a.getDimensionPixelOffset(R.styleable.DotProgress_dotPadding, dotPadding);
        headTextSize = a.getDimensionPixelSize(R.styleable.DotProgress_headTextSize, sp12);
        headTextUnitSize = a.getDimensionPixelSize(R.styleable.DotProgress_headTextUnitSize, sp5);
        int indicatorDrawableId = a.getResourceId(R.styleable.DotProgress_indicatorDrawable, R.drawable.progress_indicator);
        progressMax = a.getInt(R.styleable.DotProgress_progressMax, 100);
        progress = a.getInt(R.styleable.DotProgress_progress, 0);

        a.recycle();

        if (indicatorDrawableId > 0)
            indicatorBitmap = BitmapFactory.decodeResource(getResources(), indicatorDrawableId);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        Paint.FontMetrics fm = new Paint.FontMetrics();
        textPaint.getFontMetrics(fm);
        textHeight = fm.bottom - fm.top;

        path = new Path();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float width = getWidth();
                float height = getHeight();

                float percent = width > height ? Math.max(width, height) / Math.min(width, height) : 1;
                float offsetY = width > height ? textHeight / 2 : 0;
                centerX = width / 2;
                centerY = height / 2 * percent - offsetY;
                outRadius = Math.min(width, height) / 2;

                float angle = 270;
                float startAngle = -135 - 90;
                RectF area = new RectF(centerX - outRadius, centerY - outRadius, centerX + outRadius, centerY + outRadius);

                path.reset();
                path.addArc(area, startAngle, angle);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        percent = 66;

        canvas.save();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawPath(path, paint);

        double sin = Math.sin(Math.toRadians(1));
        float dotRadius = (float) (sin * outRadius / (1 + sin));

        paint.setStyle(Paint.Style.FILL);
        int count = 0;
        canvas.rotate(-135, centerX, centerY);
        float cotCenterY = centerY - outRadius + dotRadius + dotPadding + strokeWidth / 2;
        while (count++ < 100) {
            if (count == percent && indicatorBitmap != null)
                canvas.drawBitmap(indicatorBitmap, centerX - indicatorBitmap.getWidth() / 2,
                        centerY - indicatorBitmap.getHeight(), paint);

            canvas.drawCircle(centerX, cotCenterY, dotRadius, paint);
            canvas.rotate(2.7f, centerX, centerY);
        }
        canvas.restore();

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        float textWidth = textPaint.measureText(bottomText);
        canvas.drawText(bottomText, centerX - textWidth / 2, getHeight() - textHeight, textPaint);

        textPaint.setTextSize(headTextSize);
        float headTextWidth = textPaint.measureText(String.valueOf(percent));

        canvas.drawText(String.valueOf(percent), centerX - headTextWidth / 2, centerY - outRadius * 0.6f, textPaint);
        textPaint.setTextSize(headTextUnitSize);
        canvas.drawText(headUnitText, centerX + headTextWidth / 2, centerY - outRadius * 0.6f, textPaint);

    }

    /**
     * Setter and Getter
     */

    public synchronized int getProgressMax() {
        return progressMax;
    }

    public synchronized void setProgressMax(int progressMax) {
        if (progressMax < 0) {
            throw new IllegalArgumentException("progressMax mustn't smaller than 0");
        }
        this.progressMax = progressMax;
    }


    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度
     * 同步，允许多线程访问
     *
     * @param progress 进度
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0 || progress > progressMax) {
            throw new IllegalArgumentException("progress did not hit in 0 to progressMax");
        }
        this.progress = progress;
        percent = progress / progressMax * 100;
        postInvalidate();
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public String getHeadUnitText() {
        return headUnitText;
    }

    public void setHeadUnitText(String headUnitText) {
        this.headUnitText = headUnitText;
    }
}
