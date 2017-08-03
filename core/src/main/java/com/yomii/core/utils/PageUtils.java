package com.yomii.core.utils;


import android.content.Context;
import android.content.Intent;

import com.yomii.core.RecyclerDecorationActivity;

/**
 * 页面导航工具类
 *
 * Created by Yomii on 2017/1/24.
 */

public class PageUtils {

    private PageUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static void decorationInRecycler(Context context){
        context.startActivity(new Intent(context, RecyclerDecorationActivity.class));
    }


}
