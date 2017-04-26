package cn.yomii.www.projectmodel.net.http;

import cn.yomii.www.projectmodel.bean.response.ResponseBean;

import cn.yomii.www.projectmodel.BuildConfig;


/**
 * API请求网址
 * Created by Yomii on 2017/2/13.
 */

public class Urls {


    /**
     * 【正式地址】：正式服务器地址。
     */
    private static final String URL_FORMAL = "";
    /**
     * 开发、调试接口地址（内网、公网同时有效）
     */
    private static final String URL_DEBUG = "";
    /**
     * 模拟接口地址（内网、公网同时有效）
     * 模拟真实服务器环境的地址
     */
    public static final String URL_TEST = "";
    /**
     * 【应急地址】：未来作为故障应急使用。
     */
    public static final String URL_BACKUP = "";


    /**
     * API地址
     */
    private static String mainUrl = BuildConfig.LOG_DEBUG ? URL_DEBUG : URL_FORMAL;

    public static String getMainUrl() {
        return mainUrl;
    }

    public static void setMainUrl(String newUrl) {
        mainUrl = newUrl;
    }

    public static void fillUrlInfoAfterQuery(ResponseBean response){
        // TODO: 2017/1/14
    }

    public static void fillFromCache() {
        // TODO: 2017/1/14
    }
}
