package cn.yomii.www.projectmodel.net.http;

import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;

import cn.yomii.www.projectmodel.bean.request.RequestBean;
import cn.yomii.www.projectmodel.bean.response.ResponseBean;

/**
 * Http请求二次封装
 * Created by Yomii on 2017/1/9.
 */
public class HttpHelper {

    public static final int DEFAULT_MILLISECONDS = 20000; //默认的超时时间

    private static String url = "";

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        HttpHelper.url = url;
    }

    public static void fillUrlInfoAfterQuery(ResponseBean response){
        // TODO: 2017/1/14
    }

    public static void fillFromCache() {
        // TODO: 2017/1/14
    }

    /**
     * 异步postBean
     */
    public static void enqueue(RequestBean request, Object tag, AbsCallback callback) {
        String requestJsonString = JSON.toJSONString(request);
        LogUtils.i("requestJsonString--" + requestJsonString);
        OkGo.post(url)
                .tag(tag)
                .upJson(requestJsonString)
                .execute(callback);
    }

    /**
     * 异步post字符串
     */
    public static void enqueue(String str, Object tag, AbsCallback callback) {
        LogUtils.i("requestJsonString--" + str);
        OkGo.post(url)
                .tag(tag)
                .upJson(str)
                .execute(callback);
    }
}
