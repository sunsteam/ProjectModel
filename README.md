# ProjectModel 2017.1.12
项目快速开发模版,配置公共组件,基类等

### Application初始化
网络库,安全退出app的逻辑,提供全局Handler,Context,Metrics,Thread,Tid

### Log
'com.apkfuns.logutils:library:1.4.0'

### http连接库
'com.lzy.net:okgo:2.1.4' ,内置连接核心为okhttp-3.4.1, 配置了JsonCallback回调基类, 请求/返回结果的包装基类

**需要在App类中设置服务器证书**

https://github.com/jeasonlzy/okhttp-OkGo

### 图片库
'com.github.bumptech.glide:glide:3.7.0'

配置 GlideModule通用实现,使用OKHttp作为网络连接核心, 添加圆形/圆角图片的转换类

### android.support
'com.android.support:design:25.1.0' 包含RecyclerView和v7,v4,不需要另外导入

### Json转换
'com.alibaba:fastjson:1.2.8'

### 异常处理 and 软件升级
compile 'com.tencent.bugly:crashreport_upgrade:1.2.1'

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
