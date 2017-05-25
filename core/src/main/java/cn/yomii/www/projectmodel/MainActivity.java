package cn.yomii.www.projectmodel;

import android.util.SparseArray;

import java.util.ArrayList;

import cn.yomii.www.frame.base.BaseActivity;

/**
 * Created by Yomii on 2017/4/20.
 */

public class MainActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.main_act);

        RadarView radarView = (RadarView) findViewById(R.id.radar_view);

        ArrayList<RadarView.Argument> arguments = new ArrayList<>();
        arguments.add(new RadarView.Argument("备案信息", 10));
        arguments.add(new RadarView.Argument("技术指标", 10));
        arguments.add(new RadarView.Argument("参编单位", 10));
        arguments.add(new RadarView.Argument("产品数量", 10));
        arguments.add(new RadarView.Argument("标准数量", 10));
        radarView.setArgumentList(arguments);

        SparseArray<float[]> sparseArray = new SparseArray<>();
        sparseArray.append(0x888FCE1F,new float[]{5,3.8f,8.6f,7,5});
        sparseArray.append(0x8800A0E9,new float[]{8,9,7.8f,9,3.5f});

        radarView.setLayerList(sparseArray);
    }

    @Override
    protected void initData() {

    }
}
