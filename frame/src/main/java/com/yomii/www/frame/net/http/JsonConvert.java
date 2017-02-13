package com.yomii.www.frame.net.http;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.convert.Converter;
import com.yomii.www.frame.bean.response.ResponseBean;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
            Log.e("reason",e.toString());
            Log.e("ErrorJsonString",json);
            Log.e("ErrorClass",t.toString());
            throw e;
        }
    }
}
