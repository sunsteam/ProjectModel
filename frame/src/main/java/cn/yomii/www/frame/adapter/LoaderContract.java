package cn.yomii.www.frame.adapter;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.yomii.www.frame.bean.ListRequest;

/**
 * 列表数据获取器接口
 * Created by Yomii on 2016/9/21.
 */
public interface LoaderContract {

    int STATE_LOADING = 1;
    int STATE_NOMORE = 2;
    int STATE_HASMORE = 3;
    int STATE_EMPTY = 4;
    int STATE_ERROR = 5;
    int STATE_DATASOURCEERROR = 6;

    @IntDef({STATE_LOADING, STATE_NOMORE, STATE_HASMORE, STATE_EMPTY, STATE_ERROR, STATE_DATASOURCEERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {
    }


    /**
     * @return loader当前状态
     */
    @State
    int getState();


    /**
     * 设定loader状态
     *
     * @param state 新state
     */
    void setState(@State int state);


    /**
     * 获取数据
     */
    void load();


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
        void onLoadBefore(int state, ListRequest request);

        /**
         * 请求完成后调用,无论成功失败
         *
         * @param state 请求完成后的状态
         */
        void onLoadAfter(int state);

    }


    /**
     * 数据加载回调接口
     *
     * @param <X> 返回结果的封装
     */
    interface LoaderAdapter<X> {

        /**
         * 子类可通过复写此方法过滤数据,
         *
         * @param response 返回的结果封装
         *
         * @return 如果数据符合条件，则返回true，否则false
         */
        boolean onDataFilter(X response);

        /**
         * 获取到符合条件的数据
         *
         * @param response ListResponse
         */
        void onFilterSuccess(X response);
    }
}
