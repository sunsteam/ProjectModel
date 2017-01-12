package cn.yomii.www.projectmodel.adapter;

import android.support.annotation.NonNull;

import com.lzy.okgo.request.BaseRequest;

import cn.yomii.www.projectmodel.Info;
import cn.yomii.www.projectmodel.bean.request.ListRequestBean;
import cn.yomii.www.projectmodel.bean.response.ListResponseBean;
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

    protected ListCallback callback;

    @Override
    public void setRequest(@NonNull R request) {
        this.request = request;
        callback = new ListCallback();
    }

    @Override
    public void refreshData() {
        state = STATE_HASMORE;
        request.token = Info.getToken();
        request.pageindex = -1;
        loadNextPage();
    }

    @Override
    public void loadNextPage() {
        state = STATE_LOADING;
        request.pageindex++;
        HttpHelper.enqueue(request, this, callback);
    }

    class ListCallback extends JsonCallback<Z> {

        @Override
        public void onBefore(BaseRequest baseRequest) {
            super.onBefore(baseRequest);
            if (listener != null) {
                listener.onLoadBefore(state, request);
            }
        }

        @Override
        public void onSuccess(Z z, Call call, Response response) {
            if (listener != null && listener.dataFilter(z)) {
                //更新页数
                request.pageindex = z.pageindex;
                listener.onLoadSuccess(z);
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
