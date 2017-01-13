package cn.yomii.www.projectmodel.adapter;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.yomii.www.projectmodel.Info;
import cn.yomii.www.projectmodel.bean.request.ListRequestBean;
import cn.yomii.www.projectmodel.bean.response.ListResponseBean;


/**
 * 列表数据获取器基类
 * Created by Yomii on 2017/1/11.
 */

public abstract class AbsLoader<R extends ListRequestBean, Z extends ListResponseBean>
        implements Loader<R, Z> {

    /**
     * 数据状态
     */
    protected int state = STATE_HASMORE;

    protected R request;

    protected OnLoadListener<Z> listener;

    public AbsLoader() {
        Type[] type = getClass().getGenericInterfaces();
        if (!(type[0] instanceof ParameterizedType))
            throw new IllegalStateException("泛型类型未定义");
    }

    @Override
    public void requestData(R request) {
        this.request = request;
        refreshData();
    }

    @Override
    public void refreshData() {
        state = STATE_HASMORE;
        request.token = Info.getToken();
        request.pageindex = -1;
        loadNextPage();
    }

    @NonNull
    @Override
    public R getRequest() {
        return request;
    }

    @Override
    public void setState(@State int state) {
        this.state = state;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setLoadListener(OnLoadListener<Z> l) {
        this.listener = l;
    }

    @Override
    public OnLoadListener getLoadListener() {
        return listener;
    }
}
