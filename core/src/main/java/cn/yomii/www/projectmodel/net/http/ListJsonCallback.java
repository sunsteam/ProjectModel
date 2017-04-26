package cn.yomii.www.projectmodel.net.http;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import cn.yomii.www.projectmodel.Info;
import cn.yomii.www.projectmodel.R;
import cn.yomii.www.projectmodel.bean.request.LoginRequest;
import cn.yomii.www.projectmodel.bean.response.ListResponseBean;
import cn.yomii.www.projectmodel.bean.response.LoginResponse;
import cn.yomii.www.projectmodel.utils.ToastUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Json格式数据回调基类
 * Created by Yomii on 2017/1/9.
 */
public abstract class ListJsonCallback<T extends ListResponseBean> extends AbsCallback<T> {

    private BaseRequest baseRequest;
    private int tryCount = 3;
    private Class<?> clazz;

    public ListJsonCallback(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ListJsonCallback() {
    }

    @Override
    public void onBefore(BaseRequest request) {
        baseRequest = request;
        super.onBefore(request);
    }

    @Override
    public T convertSuccess(Response response) throws Exception {

        T o = parseJson(getBodyString(response), clazz);

        if (o.getErr() == 0)
            return o;

        throw new BusinessException(o.getErr(), o.getError());
    }

    @NonNull
    private String getBodyString(Response response) throws IOException {
        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }

    private T parseJson(String json, Type t) {
        try {
            return JSON.parseObject(json, t);
        } catch (RuntimeException e) {
            LogUtils.e("reason" + e.toString());
            LogUtils.e("ErrorJsonString" + json);
            LogUtils.e("ErrorClass" + t.toString());
            throw e;
        }
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        if (e instanceof BusinessException) {
            BusinessException re = (BusinessException) e;
            if (re.getErr() == 9984) {
                onTokenExpired(call, response);
            } else {
                onExceptionResponse(re, call, response);
            }
        }else
            LogUtils.e(e);

    }

    protected void onExceptionResponse(BusinessException e, Call call, Response response) {
        ToastUtils.imitShowToast(e.getError());
    }

    protected void onTokenExpired(Call call, Response response) {
        if (tryCount >= 1) {
            LogUtils.i("重试登陆--" + tryCount);
            tryCount--;
            HttpHelper.post(LoginRequest.getRetryLoginRequest(), "login").execute(new RetryLoginCallBack());
        } else {
            ToastUtils.imitShowToast(R.string.error_9984_token_expired);
        }
    }

    class RetryLoginCallBack extends JsonCallback<LoginResponse> {

        @Override
        public void onSuccess(LoginResponse loginResponse, Call call, Response response) {

            Info.fillUserInfoAfterLogin(loginResponse);

            if (baseRequest instanceof PostRequest) {
                try {
                    Field json = baseRequest.getClass().getDeclaredField("content");
                    json.setAccessible(true);
                    String jsonStr = (String) json.get(baseRequest);
                    LogUtils.i("原始body--" + jsonStr);

                    String tokenTag = "token\":\"";
                    if (jsonStr.contains(tokenTag)) {
                        int tokenHead = jsonStr.indexOf(tokenTag);
                        int tokenStart = tokenHead + tokenTag.length();
                        String left = jsonStr.substring(0, tokenStart);
                        int tokenEnd = jsonStr.indexOf("\"", tokenStart);
                        String right = jsonStr.substring(tokenEnd);
                        jsonStr = left + Info.getToken() + right;
                    }
                    LogUtils.i("新body--" + jsonStr);

                    HttpHelper.post(jsonStr, call.request().tag()).execute(ListJsonCallback.this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("not post request");
            }
        }
    }
}
