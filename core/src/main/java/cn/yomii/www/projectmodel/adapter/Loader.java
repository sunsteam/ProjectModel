package cn.yomii.www.projectmodel.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.yomii.www.projectmodel.bean.request.ListRequestBean;
import cn.yomii.www.projectmodel.bean.response.ListResponseBean;

/**
 * 列表数据获取器接口
 * Created by Yomii on 2016/9/21.
 */
public interface Loader<R extends ListRequestBean, Z extends ListResponseBean> {

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
     * 设置新的request请求
     *
     * @param request 请求
     */
    void requestData(R request);

    /**
     * 清除数据并重新请求当前Request的0页
     */
    void refreshData();

    /**
     * 加载下一页
     */
    void loadNextPage();


    /**
     * 获取当前请求
     *
     * @return ListRequestBean
     */
    @NonNull
    R getRequest();

    void setRequest(@NonNull R request);


    /**
     * 设定loader状态
     *
     * @param state 新state
     */
    void setState(@State int state);

    /**
     * @return loader当前状态
     */
    @State
    int getState();


    /**
     * 数据加载回调接口
     *
     * @param <Z> 数据包装对象
     */
    interface OnLoadListener<Z extends ListResponseBean> {
        /**
         * 开始请求前调用
         *
         * @param state   请求前的状态
         * @param request 当前请求
         */
        void onLoadBefore(int state, ListRequestBean request);

        /**
         *  子类可通过复写此方法过滤数据,
         *
         *  @param response 返回的结果封装
         *
         *  @return 如果不想要父类处理事件, 返回false,反之则true
         *
         * */
        boolean dataFilter(Z response);

        /**
         * 获取数据成功,err == 0
         *
         * @param response ListResponseBean
         */
        void onLoadSuccess(Z response);

        /**
         * 请求完成后调用,无论成功失败
         *
         * @param state 请求完成后的状态
         */
        void onLoadAfter(int state);
    }

    void setLoadListener(OnLoadListener<Z> l);

    OnLoadListener getLoadListener();

}
