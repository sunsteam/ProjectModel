package cn.yomii.www.frame.bean.request;


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

    public ListRequestBean(String cmd, String newToken) {
        this(cmd, 15, 0, 1, newToken);
    }

    public ListRequestBean(String cmd, int pagesize, String newToken) {
        this(cmd, pagesize, 0, 1, newToken);
    }

    public ListRequestBean(String cmd, int pagesize, int pageindex, int desc, String newToken) {
        super(cmd,newToken);
        this.pagesize = pagesize;
        this.pageindex = pageindex;
        this.desc = desc;
    }
}
