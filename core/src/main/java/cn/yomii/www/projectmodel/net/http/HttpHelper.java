package cn.yomii.www.projectmodel.net.http;

import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.request.BaseBodyRequest;
import com.lzy.okgo.request.PostRequest;
import cn.yomii.www.projectmodel.bean.request.RequestBean;

/**
 * Http请求二次封装
 * Created by Yomii on 2017/1/9.
 */
public class HttpHelper {

    public static final int DEFAULT_MILLISECONDS = 20000; //默认的超时时间

    /**
     * post字符串
     */
    public static BaseBodyRequest<PostRequest> post(String str, Object tag) {
        LogUtils.i("requestJsonString--" + str);
        return OkGo.post(Urls.getMainUrl())
                .tag(tag)
                .upJson(str);
    }

    /**
     * postBean
     */
    public static BaseBodyRequest<PostRequest> post(Object request, Object tag) {
        String requestJsonString = JSON.toJSONString(request);
        return post(requestJsonString, tag);
    }


    /**
     * post and cache
     */
    public static BaseBodyRequest<PostRequest> postAndCache(RequestBean request, Object tag, String cacheKey) {
        String requestJsonString = JSON.toJSONString(request);
        return post(requestJsonString, tag)
                .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                .cacheKey(cacheKey);
    }
}
