package cn.yomii.www.projectmodel.net.http;

import android.util.Log;

import com.alibaba.fastjson.JSONReader;
import com.apkfuns.logutils.LogUtils;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.yomii.www.projectmodel.Info;
import cn.yomii.www.projectmodel.bean.request.LoginRequest;
import cn.yomii.www.projectmodel.bean.response.LoginResponse;
import cn.yomii.www.projectmodel.bean.response.ResponseBean;
import cn.yomii.www.projectmodel.utils.ToastUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Json格式数据回调基类
 * Created by Yomii on 2017/1/9.
 *
 * @param <R> Response 返回结果封装
 */
public abstract class JsonCallback<R extends ResponseBean> extends AbsCallback<R> {

    private BaseRequest baseRequest;
    private int tryCount = 3;
    private Class<R> clazz;
    private Type type;


    public JsonCallback(Class<R> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback() {
    }

    @Override
    public void onBefore(BaseRequest request) {
        baseRequest = request;
        super.onBefore(request);
    }

    @Override
    public R convertSuccess(Response response) throws Exception {

        if (type == null) {
            if (clazz != null) {
                type = clazz;
            } else {
                ParameterizedType genType = (ParameterizedType) getClass().getGenericSuperclass();
                type = genType.getActualTypeArguments()[0];
            }
        }

        R o = parseJson(response, type);

        if (o.getErr() == 0)
            return o;

        throw new BusinessException(o.getErr(), o.getError());
    }


    private R parseJson(Response response, Type t) {
        try {
            JSONReader jsonReader = new JSONReader(response.body().charStream());
            return jsonReader.readObject(t);
        } catch (RuntimeException e) {
            Log.e("ErrorClass", t.toString());
            try {
                Log.e("ErrorJsonString", response.body().string());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            throw e;
        } finally {
            if (response != null)
                response.close();
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
        } else
            e.printStackTrace();

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
            ToastUtils.imitShowToast(cn.yomii.www.projectmodel.R.string.error_9984_token_expired);
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

                    HttpHelper.post(jsonStr, call.request().tag()).execute(JsonCallback.this);
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
