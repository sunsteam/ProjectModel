package cn.yomii.www.frame.adapter;

import android.support.annotation.NonNull;

import java.util.List;

import cn.yomii.www.frame.base.BaseRecyclerAdapter;
import cn.yomii.www.frame.bean.ListResponse;

import static cn.yomii.www.frame.adapter.LoaderContract.STATE_EMPTY;
import static cn.yomii.www.frame.adapter.LoaderContract.STATE_HASMORE;
import static cn.yomii.www.frame.adapter.LoaderContract.STATE_NOMORE;

/**
 * 通过数据获取器自动获取数据的RecyclerView适配器基类
 * Created by Yomii on 2017/1/13.
 */
public abstract class LoadRecyclerAdapter<T, Z extends ListResponse<T>> extends BaseRecyclerAdapter<T>
        implements LoaderContract.LoaderAdapter<Z> {

    ListLoader<?, Z> loaderImp;

    public LoadRecyclerAdapter(@NonNull ListLoader<?, Z> loaderImp) {
        this.loaderImp = loaderImp;
        this.loaderImp.setLoadAdapter(this);
    }

    public ListLoader<?, Z> getLoader() {
        return loaderImp;
    }

    /**
     * @param response 根据返回的ListResponse内容自动更新数据列表 固定写法
     */
    protected void checkState(Z response) {
        //        if (response.pageindex == 0)
        //            dataList.clear();
        List<T> records = response.getData();
        if (records != null && records.size() > 0) {
            loaderImp.setState(records.size() < loaderImp.getPageSize() ? STATE_NOMORE : STATE_HASMORE);
            //List增加数据
            addDataFromList(records);
        } else {
            loaderImp.setState(dataList.size() > 0 ? STATE_NOMORE : STATE_EMPTY);
            notifyDataSetChanged();
        }
    }


    @Override
    public boolean onDataFilter(Z response) {
        return true;
    }

    @Override
    public void onFilterSuccess(Z response) {
        checkState(response);
    }
}
