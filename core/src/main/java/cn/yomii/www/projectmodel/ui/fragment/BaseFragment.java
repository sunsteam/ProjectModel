package cn.yomii.www.projectmodel.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;

/**
 * Fragment基类
 *
 *
 * Created by Yomii on 2016/3/9.
 */
public abstract class BaseFragment extends Fragment {

    protected Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData(view,savedInstanceState);
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState);

    protected abstract void initData(View view, Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(this);
        super.onDestroyView();
    }
}
