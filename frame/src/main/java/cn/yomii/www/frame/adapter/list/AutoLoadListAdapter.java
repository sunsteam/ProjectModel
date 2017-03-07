package cn.yomii.www.frame.adapter.list;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import cn.yomii.www.frame.adapter.Loader;
import cn.yomii.www.frame.adapter.list.viewholder.LoadMoreViewHolder;
import cn.yomii.www.frame.bean.ModelEntity;
import cn.yomii.www.frame.bean.request.ListRequestBean;
import cn.yomii.www.frame.bean.response.ListResponseBean;


/**
 * 滑动到底部自动加载下一页的ListView Adapter
 * Created by Yomii on 2017/1/12.
 */
public abstract class AutoLoadListAdapter<T extends ModelEntity, R extends ListRequestBean,
        Z extends ListResponseBean<T>> extends LoadListAdapter<T, R, Z> {

    public AutoLoadListAdapter( @NonNull Loader<R,Z> loaderImp) {
        super(loaderImp);
    }

    @Override
    public T getItem(int position) {
        if (position > dataList.size() - 1 || position < 0) {
            return null;
        }
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size() + 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == dataList.size()) {
            return 1;
        }
        return getViewTypeId(position);
    }

    /**
     * 如果内容是复杂布局, 有多类型的itemView,重写此方法
     *
     * @return 根据不同情况返回item类型
     */
    protected int getViewTypeId(int position) {
        return 0;
    }

    //--------------------------------更新底布局--------------------------------------//

    protected LoadMoreViewHolder mFootViewholder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //最后一条是底布局
        if (position == getCount() - 1) {
            if (convertView == null) {
                mFootViewholder = new LoadMoreViewHolder(parent);
            } else {
                mFootViewholder = (LoadMoreViewHolder) convertView.getTag();
            }
            if (loaderImp.getState() == Loader.STATE_HASMORE)
                loaderImp.loadPage(); //获取下一页

            mFootViewholder.setData(loaderImp.getState(), position);

            return mFootViewholder.getRootView();
        }

        return super.getView(position, convertView, parent);
    }
}
