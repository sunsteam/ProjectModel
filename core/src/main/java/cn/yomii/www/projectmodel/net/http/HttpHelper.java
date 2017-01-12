package cn.yomii.www.projectmodel.net.http;

import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;

import cn.yomii.www.projectmodel.bean.request.RequestBean;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by Yomii on 2017/1/9.
 */

public class HttpHelper {

    public static final int DEFAULT_MILLISECONDS = 20000; //默认的超时时间

    /**
     * 异步postBean
     */
    public static void enqueue(RequestBean request, Object tag, AbsCallback callback) {
        String requestJsonString = JSON.toJSONString(request);
        LogUtils.i("requestJsonString--" + requestJsonString);
        OkGo.post(URL)
                .tag(tag)
                .upJson(requestJsonString)
                .execute(callback);
    }

    /**
     * 异步post字符串
     */
    public static void enqueue(String str, Object tag, AbsCallback callback) {
        LogUtils.i("requestJsonString--" + str);
        OkGo.post(URL)
                .tag(tag)
                .upJson(str)
                .execute(callback);
    }
}
