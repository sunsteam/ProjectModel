package cn.yomii.www.projectmodel.net.http;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.convert.Converter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.yomii.www.projectmodel.bean.response.ResponseBean;
import okhttp3.Response;

/**
 * Json转换器
 * Created by Yomii on 2017/2/9.
 */
public class JsonConvert<T extends ResponseBean> implements Converter<T> {

    @Override
    public T convertSuccess(Response response) throws Exception {
        ParameterizedType genType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] params = genType.getActualTypeArguments();
        T o = parseJson(getBodyString(response), params[0]);

        if (o.err == 0)
            return o;

        throw new BusinessException(o.err, o.error);
    }

    @NonNull
    private String getBodyString(Response response) throws IOException {
        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }

    private T parseJson(String json, Type t) {
        try {
            return JSON.parseObject(json, t);
        } catch (RuntimeException e) {
            LogUtils.e("reason" + e.toString());
            LogUtils.e("ErrorJsonString" + json);
            LogUtils.e("ErrorClass" + t.toString());
            throw e;
        }
    }
}
