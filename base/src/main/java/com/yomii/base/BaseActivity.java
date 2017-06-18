package com.yomii.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * Activity基类
 * Created by Yomii on 2016/4/13.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getName();
    private boolean isDestroyed = false;

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onInitVariableOrFlag();
        initView();
        initData();
    }

    protected void onInitVariableOrFlag() {

    }

    protected abstract void initView();

    protected abstract void initData();

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void moveBack(View v) {
        //todo TDevice.hideSoftKeyboard(v);
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
    }
}
