package com.yomii.http_okgo.adapter;

import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.request.BaseRequest;
import com.yomii.base.BaseLoadRecyclerAdapter;
import com.yomii.base.BusinessException;
import com.yomii.base.bean.ListResponse;
import com.yomii.base.bean.RequestBean;
import com.yomii.http_okgo.HttpHelper;
import com.yomii.http_okgo.JsonCallback;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Yomii on 2017/6/15.
 * <p>
 * 根据RequestBean 从网络获取数据的适配器, 只支持post方式
 *
 * @param <D> data
 */

public abstract class WebRecyclerAdapter<D, VH extends RecyclerView.ViewHolder>
        extends BaseLoadRecyclerAdapter<D, VH> {

    private RequestBean request;

    private ListCallback callback;

    public WebRecyclerAdapter(RequestBean request, Type responseType) {
        this.request = request;
        callback = new ListCallback(responseType);
    }

    @Override
    protected void onLoad() {
        JSONObject reqJson = (JSONObject) JSONObject.toJSON(request);
        reqJson.put("index", index);
        reqJson.put("size", pageSize);
        HttpHelper.post(reqJson.toJSONString(), request.cmd).execute(callback);
    }


    private class ListCallback extends JsonCallback<ListResponse<D>> {

        ListCallback(Type type) {
            super(type);
        }

        @Override
        public void onBefore(BaseRequest baseRequest) {
            super.onBefore(baseRequest);
            onLoadBefore();
        }

        @Override
        public void onSuccess(ListResponse<D> resp, Call call, Response response) {
            onLoadSuccess(resp);
        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            onLoadError();
        }

        @Override
        protected void onExceptionResponse(BusinessException e, Call call, Response response) {
            super.onExceptionResponse(e, call, response);
            onLoadException();
        }

        @Override
        public void onAfter(ListResponse<D> tListResponse, Exception e) {
            super.onAfter(tListResponse, e);
            onLoadAfter();
        }
    }
}
