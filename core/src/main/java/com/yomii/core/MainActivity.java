package com.yomii.core;

import android.view.View;

import com.yomii.base.BaseActivity;
import com.yomii.core.utils.PageUtils;

/**
 * Created by Yomii on 2017/4/20.
 * <p>
 * 主界面
 */

public class MainActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.main_act);
    }


    @Override
    protected void initData() {

    }

    /**
     * 演示RecyclerView中的Decoration自定义
     */
    public void decorationInRecycler(View view) {
        PageUtils.decorationInRecycler(this);
    }
}
