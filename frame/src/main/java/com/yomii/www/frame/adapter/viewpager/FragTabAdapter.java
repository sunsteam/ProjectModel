package com.yomii.www.frame.adapter.viewpager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.yomii.www.frame.bean.PageFragInfo;

/**
 * 用于ViewPager+Fragment组合滑动页面, 布局包含TabLayout可以自动配置标题头部
 *
 * Created by Yomii on 2016/3/31.
 */
public class FragTabAdapter extends FragmentPagerAdapter {

    private List<PageFragInfo> dataList;
    private ViewPager mViewPager;
    private TabLayout mTab;

    public FragTabAdapter(FragmentManager fm, ViewPager vp, List<PageFragInfo> data,
                          TabLayout tab) {
        super(fm);

        if (vp == null)
            throw new IllegalArgumentException("viewPager can't be null");

        mViewPager = vp;
        mViewPager.setAdapter(this);

        dataList = new ArrayList<>();
        if (data != null)
            dataList.addAll(data);

        if (tab != null) {
            mTab = tab;
            mTab.setupWithViewPager(mViewPager);
        }
    }

    public FragTabAdapter(FragmentManager fm, ViewPager vp) {
        this(fm, vp, null, null);
    }

    public FragTabAdapter(FragmentManager fm, ViewPager vp, List<PageFragInfo> data) {
        this(fm, vp, data, null);
    }

    public ViewPager getViewPager() {
        return mViewPager;
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

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);

        if (mTab != null) {
            int tabCount = mTab.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tabAt = mTab.getTabAt(i);
                int resId = dataList.get(i).iconRes;
                if (tabAt != null && resId > 0) {
                    tabAt.setIcon(resId);
                }
            }
        }
    }
}