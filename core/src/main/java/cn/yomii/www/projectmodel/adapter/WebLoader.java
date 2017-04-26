package cn.yomii.www.projectmodel.adapter;

import android.support.annotation.NonNull;

import com.lzy.okgo.request.BaseRequest;

import cn.yomii.www.frame.adapter.ListLoader;
import cn.yomii.www.frame.bean.ListRequest;
import cn.yomii.www.projectmodel.bean.response.ListResponseBean;
import cn.yomii.www.projectmodel.net.http.HttpHelper;
import cn.yomii.www.projectmodel.net.http.ListJsonCallback;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 从网络获取列表数据的获取器实现
 * Created by Yomii on 2017/1/12.
 */
public class WebLoader<R, Z extends ListResponseBean> extends ListLoader<R, Z> {

    private ListCallback callback;

    private CustomAspectListener listener;

    public WebLoader(@NonNull ListRequest<R> request) {
        super(request);
    }

    @Override
    public OnLoadAspectListener getLoadListener() {
        if (listener != null)
            return listener;
        else
            return super.getLoadListener();
    }

    @Override
    public void setLoadListener(OnLoadAspectListener l) {
        if (l instanceof CustomAspectListener)
            listener = (CustomAspectListener) l;
        else
            super.setLoadListener(l);
    }

    @Override
    public void setRequestAndResponseType(@NonNull R request, @NonNull Class<Z> clazz) {
        super.setRequestAndResponseType(request, clazz);
        callback = new ListCallback(getResponseType());
    }

    @Override
    public void load() {
        setState(STATE_LOADING);
        HttpHelper.post(request, this).execute(callback);
    }

    class ListCallback extends ListJsonCallback<Z> {

        public ListCallback(Class<?> clazz) {
            super(clazz);
        }

        @Override
        public void onBefore(BaseRequest baseRequest) {
            super.onBefore(baseRequest);
            if (listener != null) {
                listener.onLoadBefore(getState(), request);
            }
        }

        @Override
        public void onSuccess(Z z, Call call, Response response) {
            if (adapter != null && adapter.onDataFilter(z)) {
                //更新页数
                request.setPageIndex(z.getPageIndex());

                if (listener != null)
                    listener.onDataFiltered(z.getPageIndex());
                adapter.onFilterSuccess(z);
            }
        }

        @Override
        public void onAfter(Z z, Exception e) {
            super.onAfter(z, e);
            if (listener != null) {
                listener.onLoadAfter(getState());
            }
        }
    }


}
