package com.yomii.core.utils.common;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 设备相关工具类
 */
@SuppressWarnings("unused")
public class DeviceUtils {

    public static String getUniqueId(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = tm.getDeviceId() + Build.SERIAL;
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    /**
     * 作 者 : YX_Sun 创建日期 ： 2015-12-14 下午9:28:46
     * <p>
     * 描 述 ：返回规则的的MD5值
     * <p>
     * ============================================================
     **/
    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }
}
