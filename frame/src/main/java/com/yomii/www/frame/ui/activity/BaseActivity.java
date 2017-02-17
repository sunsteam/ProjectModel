package com.yomii.www.frame.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Activity基类
 * Created by Yomii on 2016/4/13.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private boolean isDestroyed = false;

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        // 6.0以上开启亮色字体
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setOverflowShowingAlways();
        initView();
        initData();
    }


    protected abstract void initView();

    protected abstract void initData();


    /**
     * 反射 显示菜单item图标
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 反射 菜单永久显示
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveBack(View v) {
        //todo TDevice.hideSoftKeyboard(v);
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        OkGo.getInstance().cancelTag(this);
        super.onDestroy();
    }
}
