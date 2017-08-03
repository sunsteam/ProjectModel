package com.yomii.http_okgo;

import android.util.Log;

import com.alibaba.fastjson.JSONReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.yomii.base.BusinessException;
import com.yomii.base.bean.ResponseBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Json格式数据回调基类
 * Created by Yomii on 2017/1/9.
 *
 * @param <R> Response 返回结果封装
 */
public abstract class JsonCallback<R extends ResponseBean> extends AbsCallback<R> {

    private Class<R> clazz;
    private Type type;


    public JsonCallback(Class<R> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback() {
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


    protected void onExceptionResponse(BusinessException e, Response<R> response) {
        Log.e("exception: ", e.getError());
    }

    protected void onTokenExpired(Response<R> response) {
    }


    @Override
    public void onError(Response<R> response) {
        super.onError(response);
        Throwable e = response.getException();
        if (e instanceof BusinessException) {
            BusinessException be = (BusinessException) e;
            if (be.getErr() == 9984) {
                onTokenExpired(response);
            } else {
                onExceptionResponse(be, response);
            }
        } else
            e.printStackTrace();
    }
}
