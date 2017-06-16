package cn.yomii.www.projectmodel.base;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.request.BaseRequest;

import java.lang.reflect.Type;

import cn.yomii.www.projectmodel.bean.request.RequestBean;
import cn.yomii.www.projectmodel.bean.response.ListResponse;
import cn.yomii.www.projectmodel.net.http.BusinessException;
import cn.yomii.www.projectmodel.net.http.HttpHelper;
import cn.yomii.www.projectmodel.net.http.JsonCallback;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Yomii on 2017/6/15.
 * <p>
 * 根据RequestBean 从网络获取数据的适配器, 只支持post方式
 *
 * @param <D> data
 */

public abstract class BaseLoaderAdapter<D> extends BaseListAdapter<D> {

    /**
     * 界面用回调
     */
    interface OnLoadAspectListener {
        /**
         * 开始请求前调用
         *
         * @param state   请求前的状态
         * @param request 当前请求
         */
        <R> void onLoadBefore(int state, R request, int index, int size);

        /**
         * 请求完成后调用,无论成功失败
         *
         * @param state 请求完成后的状态
         */
        void onLoadAfter(int state);

    }

    private RequestBean request;

    private OnLoadAspectListener listener;

    private ListCallback callback;

    public BaseLoaderAdapter(RequestBean request, Type responseType) {
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
    public boolean onDataFilter(ListResponse<D> response) {
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


    class ListCallback extends JsonCallback<ListResponse<D>> {

        public ListCallback(Type type) {
            super(type);
        }

        @Override
        public void onBefore(BaseRequest baseRequest) {
            super.onBefore(baseRequest);
            if (listener != null) {
                listener.onLoadBefore(state, request, index, pageSize);
            }
        }

        @Override
        public void onSuccess(ListResponse<D> resp, Call call, Response response) {
            if (onDataFilter(resp)) {
                //更新页数
                index = resp.getIndex();
                onFilterSuccess(resp.getData());
            }
        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            onErrorState(STATE_ERROR);
        }

        @Override
        protected void onExceptionResponse(BusinessException e, Call call, Response response) {
            super.onExceptionResponse(e, call, response);
            onErrorState(STATE_DATASOURCEERROR);
        }

        @Override
        public void onAfter(ListResponse<D> tListResponse, Exception e) {
            super.onAfter(tListResponse, e);
            if (listener != null) {
                listener.onLoadAfter(state);
            }
        }
    }



}
