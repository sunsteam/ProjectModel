package cn.yomii.www.projectmodel;

import com.apkfuns.logutils.LogUtils;

import cn.yomii.www.frame.adapter.LoaderContract;
import cn.yomii.www.frame.bean.request.ListRequestBean;
import cn.yomii.www.frame.bean.response.ListResponseBean;
import cn.yomii.www.frame.ui.activity.BaseActivity;
import cn.yomii.www.projectmodel.adapter.CustomAspectListener;
import cn.yomii.www.projectmodel.adapter.WebLoader;

/**
 * Created by Yomii on 2017/4/20.
 */

public class MainActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.main_act);

        WebLoader<Object, ListResponseBean> loader = new WebLoader<>();
        CustomAspectListener customAspectListener = new CustomAspectListener() {
            @Override
            public void onDataFiltered(int index) {

            }

            @Override
            public void onLoadBefore(int state, ListRequestBean request) {

            }

            @Override
            public void onLoadAfter(int state) {

            }
        };
        loader.setLoadListener(customAspectListener);
        LoaderContract.OnLoadAspectListener loadListener = loader.getLoadListener();

        LogUtils.i("写入: " + customAspectListener);
        LogUtils.i("获取: " + loadListener);

    }

    @Override
    protected void initData() {

    }
}
