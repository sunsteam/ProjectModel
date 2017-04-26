package cn.yomii.www.frame.adapter;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.yomii.www.frame.bean.ListRequest;


/**
 * 列表数据获取器基类
 * Created by Yomii on 2017/1/11.
 *
 * @param <R> 请求内容封装
 * @param <Z> 返回类型封装
 */

public abstract class ListLoader<R, Z> implements LoaderContract {

    /**
     * 数据状态
     */
    private int state = STATE_HASMORE;

    /**
     * 初始请求页
     */
    private int originIndex;

    protected ListRequest<R> request;

    private Class<Z> responseType;

    private OnLoadAspectListener listener;

    protected LoaderAdapter<Z> adapter;


    public ListLoader(@NonNull ListRequest<R> request) {
        Type type = getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType))
            throw new IllegalStateException("泛型类型未定义");

        originIndex = request.getPageIndex();
        this.request = request;
    }

    public void resetToOriginIndex() {
        state = STATE_HASMORE;
        request.setPageIndex(originIndex);
    }

    @NonNull
    public R getRequest() {
        return request.getRequest();
    }

    public void setPageSize(int pageSize) {
        request.setPageSize(pageSize);
    }

    public int getPageSize() {
        return request.getPageSize();
    }

    public void setIndex(int index) {
        request.setPageIndex(index);
    }

    public int getIndex() {
        return request.getPageIndex();
    }

    public int getOriginIndex() {
        return originIndex;
    }

    public void setOriginIndex(int originIndex) {
        this.originIndex = originIndex;
    }

    public void setRequestAndResponseType(@NonNull R request, @NonNull Class<Z> clazz) {
        this.request.setRequest(request);
        this.request.setPageIndex(originIndex);
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


    public OnLoadAspectListener getLoadListener() {
        return listener;
    }

    public LoaderAdapter<Z> getAdapter() {
        return adapter;
    }

    public void setLoadListener(OnLoadAspectListener l) {
        listener = l;
    }

    public void setLoadAdapter(LoaderAdapter<Z> l) {
        adapter = l;
    }


    //----------子类专用----------//

    protected Class<Z> getResponseType() {
        return responseType;
    }
}
