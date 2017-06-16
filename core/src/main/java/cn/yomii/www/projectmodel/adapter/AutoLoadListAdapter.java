package cn.yomii.www.projectmodel.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Type;

import cn.yomii.www.projectmodel.base.BaseLoaderAdapter;
import cn.yomii.www.projectmodel.base.LoaderState;
import cn.yomii.www.projectmodel.bean.request.RequestBean;


/**
 * Created by Yomii on 2017/1/12.
 * <p>
 * 滑动到底部自动加载下一页的ListView Adapter
 * <p>
 * 注意: onItemClickListener 中取得的position 比 dataList 中多1, 应注意判空和数组越界
 */
public abstract class AutoLoadListAdapter<D> extends BaseLoaderAdapter<D> {

    public AutoLoadListAdapter(RequestBean request, Type responseType) {
        super(request, responseType);
    }

    @Override
    public D getItem(int position) {
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //最后一条是底布局
        LoadMoreViewHolder mFootViewHolder;
        if (position == getCount() - 1) {
            if (convertView == null) {
                mFootViewHolder = new LoadMoreViewHolder(parent);
            } else {
                mFootViewHolder = (LoadMoreViewHolder) convertView.getTag();
            }
            if (state == LoaderState.STATE_HASMORE)
                load(); //获取下一页

            mFootViewHolder.setData(state, position);

            return mFootViewHolder.getRootView();
        }

        return super.getView(position, convertView, parent);
    }
}
