package com.yomii.http_retrofit;


import android.util.Log;

import com.yomii.base.BusinessException;
import com.yomii.base.bean.ResponseBean;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yomii on 2017/6/19.
 */

public abstract class JsonCallBack<T extends ResponseBean> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if (response.isSuccessful()) {
            T body = response.body();
            if (body.getErr() == 0)
                onSuccess(body, call.request(), response.raw());
            else
                onExceptionResponse(new BusinessException(body.getErr(), body.getError()), body);
        } else {
            Log.w("http", "http-code is not in 200-300, it is " + response.code());
        }

        onAfter(call,response,null);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(call, new Exception(t));
    }




    /**
     * 对返回数据进行操作的回调， UI线程
     */
    public abstract void onSuccess(T t, Request request, okhttp3.Response response);

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
     */
    public void onError(Call<T> call, Exception e) {
    }

    /**
     * 服务器返回的接口错误，都会回调该方法， UI线程
     */
    protected void onExceptionResponse(BusinessException e, T body) {
        Log.e("BussinessException: ", e.getError());
    }

    protected void onAfter(Call<T> call, Response<T> response, Exception e) {

    }

}
