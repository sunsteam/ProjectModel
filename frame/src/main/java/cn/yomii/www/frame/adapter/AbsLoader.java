package cn.yomii.www.frame.adapter;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.yomii.www.frame.bean.request.ListRequestBean;
import cn.yomii.www.frame.bean.response.ListResponseBean;


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

    protected OnLoadAspectListener listener;

    protected Class<Z> responseType;

    protected LoaderAdapter<Z> adapter;

    public AbsLoader() {
        Type type = getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType))
            throw new IllegalStateException("泛型类型未定义");
    }


    @Override
    public void refreshData(String token) {
        state = STATE_HASMORE;
        request.token = token;
        request.pageindex = 0;
        loadPage();
    }

    @NonNull
    @Override
    public R getRequest() {
        return request;
    }

    @Override
    public void setRequestAndResponseType(@NonNull R request, @NonNull Class<Z> clazz) {
        this.request = request;
        this.responseType = clazz;
    }

    @Override
    public void setState(@State int state) {
        this.state = state;
    }

    @Override
    public int getState() {
        return state;
    }


    @NonNull
    @Override
    public Class<Z> getResponseClass() {
        return responseType;
    }


    @Override
    public OnLoadAspectListener getLoadListener() {
        return listener;
    }

    @Override
    public void setLoadListener(OnLoadAspectListener l) {
        listener = l;
    }

    @Override
    public void setLoadAdapter(LoaderAdapter<Z> l) {
        adapter = l;
    }
}
