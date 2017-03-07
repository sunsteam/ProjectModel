package cn.yomii.www.frame.adapter.recycler;

import android.support.annotation.NonNull;

import java.util.List;

import cn.yomii.www.frame.adapter.Loader;
import cn.yomii.www.frame.bean.ModelEntity;
import cn.yomii.www.frame.bean.request.ListRequestBean;
import cn.yomii.www.frame.bean.response.ListResponseBean;

import static cn.yomii.www.frame.adapter.Loader.STATE_EMPTY;
import static cn.yomii.www.frame.adapter.Loader.STATE_HASMORE;
import static cn.yomii.www.frame.adapter.Loader.STATE_NOMORE;

/**
 * 通过数据获取器自动获取数据的RecyclerView适配器基类
 *
 * @param <T> 列表数据类型
 * @param <R> 请求数据包装类
 * @param <Z> 返回数据包装类
 *            Created by Yomii on 2017/1/13.
 */
public abstract class LoadRecyclerAdapter<T extends ModelEntity, R extends ListRequestBean, Z extends
        ListResponseBean<T>>
        extends BaseRecyclerAdapter<T>
        implements Loader.LoaderAdapter<Z> {

    Loader<R, Z> loaderImp;

    public LoadRecyclerAdapter(@NonNull Loader<R, Z> loaderImp) {
        super();
        this.loaderImp = loaderImp;
        this.loaderImp.setLoadAdapter(this);
    }

    public Loader<R, Z> getLoader() {
        return loaderImp;
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
    public boolean dataFilter(Z response) {
        return true;
    }

    @Override
    public void onLoadSuccess(Z response) {
        checkState(response);
    }

}
