package cn.yomii.www.projectmodel.adapter;

import android.support.annotation.NonNull;

import com.lzy.okgo.request.BaseRequest;

import cn.yomii.www.frame.adapter.AbsLoader;
import cn.yomii.www.frame.bean.request.ListRequestBean;
import cn.yomii.www.frame.bean.response.ListResponseBean;
import cn.yomii.www.projectmodel.net.http.HttpHelper;
import cn.yomii.www.projectmodel.net.http.JsonCallback;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 从网络获取列表数据的获取器实现
 * Created by Yomii on 2017/1/12.
 */
public class WebLoader<R extends ListRequestBean, Z extends ListResponseBean>
        extends AbsLoader<R, Z> {

    private ListCallback callback;

    @Override
    public void setRequestAndResponseType(@NonNull R request, @NonNull Class<Z> clazz) {
        super.setRequestAndResponseType(request, clazz);
        callback = new ListCallback(responseType);
    }

    @Override
    public void loadPage() {
        state = STATE_LOADING;
        HttpHelper.post(request, this).execute(callback);
    }

    class ListCallback extends JsonCallback<Z> {

        public ListCallback(Class<?> clazz) {
            super(clazz);
        }

        @Override
        public void onBefore(BaseRequest baseRequest) {
            super.onBefore(baseRequest);
            if (listener != null) {
                listener.onLoadBefore(state, request);
            }
        }

        @Override
        public void onSuccess(Z z, Call call, Response response) {
            if (adapter != null && adapter.dataFilter(z)) {
                //更新页数
                request.pageindex++;
                adapter.onLoadSuccess(z);
            }
        }

        @Override
        public void onAfter(Z z, Exception e) {
            super.onAfter(z, e);
            if (listener != null) {
                listener.onLoadAfter(state);
            }
        }
    }


}
