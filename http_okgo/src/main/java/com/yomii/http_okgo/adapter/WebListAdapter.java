package com.yomii.http_okgo.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.yomii.base.BaseLoadListAdapter;
import com.yomii.base.BusinessException;
import com.yomii.base.bean.ListResponse;
import com.yomii.base.bean.RequestBean;
import com.yomii.http_okgo.HttpHelper;
import com.yomii.http_okgo.JsonCallback;

import java.lang.reflect.Type;


/**
 * Created by Yomii on 2017/6/15.
 * <p>
 * 根据RequestBean 从网络获取数据的适配器, 只支持post方式
 *
 * @param <D> data
 */

public abstract class WebListAdapter<D> extends BaseLoadListAdapter<D> {

    private RequestBean request;
    private ListCallback callback;

    public WebListAdapter(RequestBean request, Type responseType) {
        this.request = request;
        callback = new ListCallback(responseType);
    }

    @Override
    public void onLoad() {

        JSONObject reqJson = (JSONObject) JSON.toJSON(request);
        reqJson.put("index", index);
        reqJson.put("size", pageSize);
        HttpHelper.<ListResponse<D>>post(reqJson.toJSONString(), request.cmd).execute(callback);
    }


    private class ListCallback extends JsonCallback<ListResponse<D>> {

        ListCallback(Type type) {
            super(type);
        }

        @Override
        public void onStart(Request<ListResponse<D>, ? extends Request> request) {
            super.onStart(request);
            onLoadBefore();
        }

        @Override
        public void onSuccess(Response<ListResponse<D>> response) {
            onLoadSuccess(response.body());
        }

        @Override
        public void onError(Response<ListResponse<D>> response) {
            super.onError(response);
            onLoadError();
        }

        @Override
        protected void onExceptionResponse(BusinessException e, Response<ListResponse<D>> response) {
            super.onExceptionResponse(e, response);
            onLoadException();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            onLoadAfter();
        }
    }


}
