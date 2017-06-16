package cn.yomii.www.projectmodel.utils.common;

import android.annotation.SuppressLint;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;

import java.text.NumberFormat;

/**
 * 转换工具类
 */
@SuppressWarnings("unused")
public class ConvertUtils {

    private ConvertUtils(){}

    /**
     * 以unit为单位的内存大小转字节数
     *
     * @param memorySize 大小
     * @param unit       单位类型
     *                   <ul>
     *                   <li>{@link ConstUtils.MemoryUnit#BYTE}: 字节</li>
     *                   <li>{@link ConstUtils.MemoryUnit#KB}  : 千字节</li>
     *                   <li>{@link ConstUtils.MemoryUnit#MB}  : 兆</li>
     *                   <li>{@link ConstUtils.MemoryUnit#GB}  : GB</li>
     *                   </ul>
     * @return 字节数
     */
    public static long memorySize2Byte(long memorySize, ConstUtils.MemoryUnit unit) {
        if (memorySize < 0) return -1;
        switch (unit) {
            default:
            case BYTE:
                return memorySize;
            case KB:
                return memorySize * ConstUtils.KB;
            case MB:
                return memorySize * ConstUtils.MB;
            case GB:
                return memorySize * ConstUtils.GB;
        }
    }

    /**
     * 字节数转以unit为单位的内存大小
     *
     * @param byteNum 字节数
     * @param unit    单位类型
     *                <ul>
     *                <li>{@link ConstUtils.MemoryUnit#BYTE}: 字节</li>
     *                <li>{@link ConstUtils.MemoryUnit#KB}  : 千字节</li>
     *                <li>{@link ConstUtils.MemoryUnit#MB}  : 兆</li>
     *                <li>{@link ConstUtils.MemoryUnit#GB}  : GB</li>
     *                </ul>
     * @return 以unit为单位的size
     */
    public static double byte2MemorySize(long byteNum, ConstUtils.MemoryUnit unit) {
        if (byteNum < 0) return -1;
        switch (unit) {
            default:
            case BYTE:
                return (double) byteNum;
            case KB:
                return (double) byteNum / ConstUtils.KB;
            case MB:
                return (double) byteNum / ConstUtils.MB;
            case GB:
                return (double) byteNum / ConstUtils.GB;
        }
    }

    /**
     * 字节数转合适内存大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    @SuppressLint("DefaultLocale")
    public static String byte2FitMemorySize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < ConstUtils.KB) {
            return String.format("%.3fB", byteNum + 0.0005);
        } else if (byteNum < ConstUtils.MB) {
            return String.format("%.3fKB", byteNum / ConstUtils.KB + 0.0005);
        } else if (byteNum < ConstUtils.GB) {
            return String.format("%.3fMB", byteNum / ConstUtils.MB + 0.0005);
        } else {
            return String.format("%.3fGB", byteNum / ConstUtils.GB + 0.0005);
        }
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(float pxValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(float spValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(float pxValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 转换Json字符串为Bean
     *
     * @param s json
     * @param clazz bean.class
     * @param <T> bean type
     *
     * @return bean
     */
    public static <T> T parseToBean(String s, Class<T> clazz) {
        try{
            return JSON.parseObject(s, clazz);
        }catch (RuntimeException e){
            LogUtils.e("reason"+e.toString());
            LogUtils.e("ErrorJsonString"+s);
            LogUtils.e("ErrorClass"+clazz.toString());
            return null;
        }
    }


    /**
     *
     * 返回百分比, 精确到2位小数
     *
     * @param p1 分子
     * @param p2 分母
     *
     * @return 百分比
     */
    public static String percent(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        str = nf.format(p3);
        return str;
    }

    /**
     *
     * 返回百分比, 个位数
     *
     * @param p1 分子
     * @param p2 分母
     *
     * @return 百分比
     */
    public static String percent2(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0);
        str = nf.format(p3);
        return str;
    }
}
