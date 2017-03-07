package cn.yomii.www.frame.bean;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 用于FragmentTabAdapter的数据包装类
 *
 * Created by Yomii on 2016/3/31.
 */
public class PageFragInfo {

    public String title;
    public int iconRes;
    public Fragment frag;
    public Bundle bundle;


    public PageFragInfo(String title, int iconRes, Fragment frag, Bundle bundle) {
        this.title = title;
        this.iconRes = iconRes;
        this.frag = frag;
        this.bundle = bundle;
    }


    public PageFragInfo(String title, int iconRes, Fragment frag) {
        this(title, iconRes, frag, null);
    }
}