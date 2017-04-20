package cn.yomii.www.frame.base;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPagerAdapter基类
 * Created by Yomii on 2016/4/19.
 */
public class BaseViewPagerAdapter<T> extends PagerAdapter {

    protected List<T> dataList;

    public BaseViewPagerAdapter(List<T> data) {
        dataList = new ArrayList<>();
        if (data != null)
            dataList.addAll(data);
    }

    public BaseViewPagerAdapter() {
        this(null);
    }


    public List<T> getDataList() {
        return dataList;
    }

    public void addDataFromList(List<T> dataList) {
        if (dataList != null) {
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void setDataList(List<T> dataList) {
        if (dataList != null) {
            this.dataList = dataList;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}