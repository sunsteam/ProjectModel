package cn.yomii.www.projectmodel;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * 简化版生命周期管理
 * Created by Yomii on 2016/7/20.
 */
public abstract class ActivityLifeCallback implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }
}
