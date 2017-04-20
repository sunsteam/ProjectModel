package cn.yomii.www.projectmodel.adapter;

import cn.yomii.www.frame.adapter.LoaderContract;

/**
 * Created by Yomii on 2017/4/20.
 */

public interface CustomAspectListener extends LoaderContract.OnLoadAspectListener {

    /**
     * 数据过滤通过时, 回调数据页数和总数
     *
     * @param index 返回数据索引
     */
    void onDataFiltered(int index);
}
