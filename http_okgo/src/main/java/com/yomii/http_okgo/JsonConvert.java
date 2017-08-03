package com.yomii.http_okgo;

import android.util.Log;

import com.alibaba.fastjson.JSONReader;
import com.lzy.okgo.convert.Converter;
import com.yomii.base.BusinessException;
import com.yomii.base.bean.ResponseBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Json转换器
 * Created by Yomii on 2017/2/9.
 */
public class JsonConvert<R extends ResponseBean> implements Converter<R> {

    private Class<R> clazz;
    private Type type;

    public JsonConvert(Class<R> clazz) {
        this.clazz = clazz;
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert() {
    }

    @Override
    public R convertResponse(okhttp3.Response response) throws Throwable {

        ResponseBody body = response.body();
        if (body == null)
            return null;

        if (type == null) {
            if (clazz != null) {
                type = clazz;
            } else {
                ParameterizedType genType = (ParameterizedType) getClass().getGenericSuperclass();
                type = genType.getActualTypeArguments()[0];
            }
        }

        R o = parseJson(body, type);

        if (o.getErr() == 0)
            return o;

        throw new BusinessException(o.getErr(), o.getError());
    }


    private R parseJson(ResponseBody body, Type t) {
        try {
            JSONReader jsonReader = new JSONReader(body.charStream());
            return jsonReader.readObject(t);
        } catch (RuntimeException e) {
            Log.e("ErrorClass", t.toString());
            Log.e("ErrorJsonString", body.toString());
            throw e;
        } finally {
            if (body != null)
                body.close();
        }
    }
}
