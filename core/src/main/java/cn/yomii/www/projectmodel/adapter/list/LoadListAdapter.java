package cn.yomii.www.projectmodel.adapter.list;

import android.support.annotation.NonNull;

import java.util.List;

import cn.yomii.www.projectmodel.adapter.Loader;
import cn.yomii.www.projectmodel.bean.Entity;
import cn.yomii.www.projectmodel.bean.request.ListRequestBean;
import cn.yomii.www.projectmodel.bean.response.ListResponseBean;

import static cn.yomii.www.projectmodel.adapter.Loader.STATE_EMPTY;
import static cn.yomii.www.projectmodel.adapter.Loader.STATE_HASMORE;
import static cn.yomii.www.projectmodel.adapter.Loader.STATE_NOMORE;

/**
 * 通过数据获取器自动获取数据的适配器基类
 *
 * @param <T> 列表数据类型
 * @param <R> 请求数据包装类
 * @param <Z> 返回数据包装类
 *            Created by Yomii on 2017/1/11.
 */
public abstract class LoadListAdapter<T extends Entity, R extends ListRequestBean, Z extends ListResponseBean>
        extends BaseListAdapter<T>
        implements Loader.OnLoadListener<Z> {

    Loader<R, Z> loaderImp;

    public LoadListAdapter(@NonNull R request,@NonNull Loader<R, Z> loaderImp) {
        super();
        this.loaderImp = loaderImp;
        this.loaderImp.setLoadListener(this);
        this.loaderImp.setRequest(request);
    }


    /**
     * @param response 根据返回的ListResponse内容自动更新数据列表 固定写法
     */
    protected void checkState(Z response) {
        if (response.pageindex == 0)
            dataList.clear();
        List<T> records = response.getRecords();
        if (records != null && records.size() > 0) {
            loaderImp.setState(records.size() < loaderImp.getRequest().pagesize ? STATE_NOMORE : STATE_HASMORE);
            //List增加数据
            addDataFromList(records);
        } else {
            loaderImp.setState(dataList.size() > 0 ? STATE_NOMORE : STATE_EMPTY);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean dataFilter(Z result) {
        return true;
    }

    @Override
    public void onLoadBefore(int state, ListRequestBean request) {
    }

    @Override
    public void onLoadSuccess(Z response) {
        checkState(response);
    }

    @Override
    public void onLoadAfter(int state) {
    }
}
