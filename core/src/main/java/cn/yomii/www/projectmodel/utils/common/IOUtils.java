package cn.yomii.www.projectmodel.utils.common;

import com.apkfuns.logutils.LogUtils;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO工具类
 */
public class IOUtils {
    /**
     * 关闭流
     */
    public static void close(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    LogUtils.e(e);
                }
            }
        }
    }
}
