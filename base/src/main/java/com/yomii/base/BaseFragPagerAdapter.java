package com.yomii.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * FragPagerAdapter 基类
 *
 * Created by Yomii on 2016/3/31.
 */
public class BaseFragPagerAdapter extends FragmentPagerAdapter {

    private List<PageFragInfo> dataList;

    public BaseFragPagerAdapter(FragmentManager fm, List<PageFragInfo> data) {
        super(fm);

        dataList = new ArrayList<>();
        if (data != null)
            dataList.addAll(data);
    }


    public List<PageFragInfo> getDataList() {
        return dataList;
    }

    @Override
    public Fragment getItem(int position) {
        return dataList.get(position).frag;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dataList.get(position).title;
    }



    /**
     * 用于FragmentTabAdapter的数据包装类
     *
     */
    public static class PageFragInfo {

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

}