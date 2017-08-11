package com.yomii.http_okgo;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.request.PostRequest;
import com.yomii.base.bean.RequestBean;

import org.json.JSONObject;

/**
 * Http请求二次封装
 * Created by Yomii on 2017/1/9.
 */
public class HttpHelper {

    public static final String URL = "";

    public static final int DEFAULT_MILLISECONDS = 20000; //默认的超时时间

    /**
     * post字符串
     */
    public static <B> PostRequest<B> post(String json, Object tag) {
        Log.i("requestJsonString--", json);
        return OkGo.<B>post(URL).tag(tag).upJson(json);

    }

    /**
     * postJsonObject
     */
    public static <B> PostRequest<B> post(JSONObject json, Object tag) {
        Log.i("requestJsonString--", json.toString());
        return OkGo.<B>post(URL).tag(tag).upJson(json);

    }

    /**
     * postBean
     */
    public static <B> PostRequest<B> post(RequestBean request, Object tag) {
        return OkGo.<B>post(URL)
                .tag(tag)
                .upJson(objToJson(request));
    }


    /**
     * post and cache
     */
    public static <B> PostRequest<B> postAndCache(RequestBean request, Object tag, String cacheKey) {
        return OkGo.<B>post(URL)
                .tag(tag)
                .upJson(objToJson(request))
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .cacheKey(cacheKey);
    }


    private static String objToJson(Object obj) {
        String json = JSON.toJSONString(obj);
        Log.i("requestJsonString--", json);
        return json;
    }
}
