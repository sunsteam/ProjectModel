# ProjectModel 2017.6.19
项目快速开发模版,配置公共组件,基类等

base : 基类library

view : 自定义控件

http_okgo : 通过OKGO封装OKHttp

http_retrofit : 通过retrofit封装OKHttp

core : app客户端, 包含各种utils

### Application初始化
网络库,安全退出app的逻辑,提供全局Handler,Context,Metrics,Thread,Tid

### Log
'com.apkfuns.logutils:library:1.4.0'

### http连接库
okhttp-3.8.0
'com.lzy.net:okgo:2.1.4'
'com.squareup.retrofit2:retrofit:2.3.0'

配置了JsonCallback回调基类, 请求/返回结果的包装基类


**需要在App类中设置服务器证书**

https://github.com/jeasonlzy/okhttp-OkGo

### 图片库
'com.github.bumptech.glide:glide:3.8.0'

配置 GlideModule通用实现,使用OKHttp作为网络连接核心, 添加圆形/圆角图片的转换类

### android.support
appcompat , RecyclerView , constraint-layout

### Json转换
'com.alibaba:fastjson:1.2.8' 对应 OKGO
Gson 对应 Retrofit

### 异常处理 and 软件升级
compile 'com.tencent.bugly:crashreport_upgrade:1.3.1'

- 异常处理

已配置完成, 默认渠道名为@string/appname。在设置sPsnUid时同步设置为userId, 设置token时作为环境变量同时设置到bugly.
在project和module的build.gradle中增加了mapping插件的支持,默认关闭mapping文件的自动上传功能, 因为影响Release打包速度

- 软件升级

已配置完成

**需要在App类中设置Bugly App Id**

**需要在module的build.gradle文件中配置bugly的app_Id和app_Key**



### 其他工具
'org.greenrobot:eventbus:3.0.0' 发布/订阅模式工具, 快速传递事件

'com.github.chrisbanes.photoview:library:1.2.4' 图片展示控件,支持手势放大/缩小/滑动预览

'se.emilsjolander:stickylistheaders:2.7.0' List黏性头部控件
