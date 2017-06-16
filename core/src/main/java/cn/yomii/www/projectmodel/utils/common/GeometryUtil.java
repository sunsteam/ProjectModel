package cn.yomii.www.projectmodel.utils.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * 几何图形工具
 */
@SuppressWarnings("unused")
public class GeometryUtil {

    /**
     * As meaning of method name. 获得两点之间的距离
     *
     */
    public static float getDistanceBetween2Points(PointF p0, PointF p1) {
        return (float) Math.sqrt(Math.pow(p0.y - p1.y, 2)
                + Math.pow(p0.x - p1.x, 2));
    }

    /**
     * Get middle point between p1 and p2. 获得两点连线的中点
     */
    public static PointF getMiddlePoint(PointF p1, PointF p2) {
        return new PointF((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
    }

    /**
     * Get point between p1 and p2 by percent. 根据百分比获取两点之间的某个点坐标
     */
    public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {
        return new PointF(evaluateValue(percent, p1.x, p2.x), evaluateValue(
                percent, p1.y, p2.y));
    }

    /**
     * 根据分度值，计算从start到end中，fraction位置的值。fraction范围为0 -> 1
     */
    public static float evaluateValue(float fraction, Number start, Number end) {
        return start.floatValue() + (end.floatValue() - start.floatValue())
                * fraction;
    }

    /**
     * Get the point of intersection between circle and line. 获取
     * 通过指定圆心，正切为lineK的直线与圆的交点。
     *
     * @param pMiddle The circle center point.
     * @param radius  The circle radius.
     * @param lineK   The slope of line which cross the pMiddle.
     *
     */
    public static PointF[] getIntersectionPoints(PointF pMiddle, float radius,
                                                 Double lineK) {
        PointF[] points = new PointF[2];

        float radian, xOffset, yOffset;
        if (lineK != null) {
            radian = (float) Math.atan(lineK);
            xOffset = (float) (Math.sin(radian) * radius);
            yOffset = (float) (Math.cos(radian) * radius);
        } else {
            xOffset = radius;
            yOffset = 0;
        }
        points[0] = new PointF(pMiddle.x + xOffset, pMiddle.y - yOffset);
        points[1] = new PointF(pMiddle.x - xOffset, pMiddle.y + yOffset);

        return points;
    }


    /**
     * 绘制圆点
     */
    public static Bitmap drawCirclePoint(int color, int radius) {
        Bitmap bitmap = Bitmap.createBitmap(2 * radius, 2 * radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(radius, radius, radius, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }
}