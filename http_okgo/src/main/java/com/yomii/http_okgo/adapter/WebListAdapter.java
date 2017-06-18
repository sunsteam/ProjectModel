package com.yomii.http_okgo.adapter;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.request.BaseRequest;
import com.yomii.base.BaseListAdapter;
import com.yomii.base.BusinessException;
import com.yomii.base.LoaderState;
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

public abstract class WebListAdapter<D> extends BaseListAdapter<D> {

    private RequestBean request;

    private OnLoadAspectListener listener;

    private ListCallback callback;

    public WebListAdapter(RequestBean request, Type responseType) {
        this.request = request;
        callback = new ListCallback(responseType);
    }


    /**
     * 根据 RequestBean , 加上 index 和 size , 获取数据
     */
    public void load() {
        JSONObject reqJson = (JSONObject) JSONObject.toJSON(request);
        reqJson.put("index", index);
        reqJson.put("size", pageSize);
        HttpHelper.post(reqJson.toJSONString(), request.cmd).execute(callback);
    }


    /**
     * 子类可通过复写此方法过滤数据, 默认不过滤
     *
     * @param response 返回的结果封装
     *
     * @return 如果数据符合条件，则返回true，否则false
     */
    protected boolean onDataFilter(ListResponse<D> response) {
        return true;
    }


    /**
     * 设置读取数据前后在View上的回调
     *
     * @param listener 读取状态切面的监听器
     */
    public void setListener(OnLoadAspectListener listener) {
        this.listener = listener;
    }


    private class ListCallback extends JsonCallback<ListResponse<D>> {

        boolean alreadyNotify;

        ListCallback(Type type) {
            super(type);
        }

        @Override
        public void onBefore(BaseRequest baseRequest) {
            super.onBefore(baseRequest);
            alreadyNotify = false;

            if (listener != null) {
                listener.onLoadBefore(state, request, index, pageSize);
            }
        }

        @Override
        public void onSuccess(ListResponse<D> resp, Call call, Response response) {
            if (onDataFilter(resp)) {
                //更新页数
                index = resp.getIndex();
                alreadyNotify = onLoadSuccess(resp.getData());
            }
        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            setState(LoaderState.STATE_ERROR);
        }

        @Override
        protected void onExceptionResponse(BusinessException e, Call call, Response response) {
            super.onExceptionResponse(e, call, response);
            setState(LoaderState.STATE_DATASOURCEERROR);
        }

        @Override
        public void onAfter(ListResponse<D> tListResponse, Exception e) {
            super.onAfter(tListResponse, e);
            if (!alreadyNotify)
                notifyDataSetInvalidated();

            if (listener != null) {
                listener.onLoadAfter(state);
            }
        }
    }


}
