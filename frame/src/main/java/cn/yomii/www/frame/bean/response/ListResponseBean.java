package cn.yomii.www.frame.bean.response;


import java.util.List;

import cn.yomii.www.frame.bean.ModelEntity;

/**
 * 数据集请求封装基类
 * Created by Yomii on 2016/9/5.
 */
public abstract class ListResponseBean<T extends ModelEntity> extends ResponseBean {

    /**
     * 对应请求的命令
     */
    public String cmd;

    /**
     * 本次返回数据的页码
     */
    public int pageindex;

    public abstract List<T> getRecords();

}