package cn.yomii.www.projectmodel.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by Yomii on 2016/3/31.
 */
public class FragTabAdapter extends FragmentPagerAdapter {

    private List<PageFragInfo> dataList;

    public FragTabAdapter(FragmentManager fm, List<PageFragInfo> data) {
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

}