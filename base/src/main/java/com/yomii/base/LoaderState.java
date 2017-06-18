package com.yomii.base;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 数据状态的描述
 *
 * Created by Yomii on 2017/6/16.
 */

public interface LoaderState {

    /**
     * 数据传输中, 不允许新的数据请求
     */
    int STATE_LOADING = 1;
    /**
     * 全部分页加载完成, 没有更多数据
     */
    int STATE_NOMORE = 2;
    /**
     * 还有更多数据
     */
    int STATE_HASMORE = 3;
    /**
     * 没有任何数据
     */
    int STATE_EMPTY = 4;
    /**
     * 网络错误
     */
    int STATE_ERROR = 5;
    /**
     * 数据源错误 : 接口或数据库出错
     */
    int STATE_DATASOURCEERROR = 6;

    @IntDef({STATE_LOADING, STATE_NOMORE, STATE_HASMORE, STATE_EMPTY, STATE_ERROR, STATE_DATASOURCEERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {
    }

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
}
