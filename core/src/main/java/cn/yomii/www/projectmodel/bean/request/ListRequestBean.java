package cn.yomii.www.projectmodel.bean.request;


import cn.yomii.www.projectmodel.Info;

/**
 * 数据集合请求封装基类
 * Created by Yomii on 2016/9/5.
 */
public class ListRequestBean extends RequestBean {

    public int pagesize;

    public int pageindex;

    /**
     * 0 升序 1 降序
     */
    public int desc = 1;

    public ListRequestBean(String cmd) {
        this(cmd, 15, -1, 1);
    }

    public ListRequestBean(String cmd, int pagesize) {
        this(cmd, pagesize, -1, 1);
    }

    public ListRequestBean(String cmd, int pagesize, int pageindex, int desc) {
        this.cmd = cmd;
        this.token = Info.getToken();
        this.pagesize = pagesize;
        this.pageindex = pageindex;
        this.desc = desc;
    }
}
