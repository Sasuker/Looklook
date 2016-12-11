package com.example.administrator.look.Util;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/11/24.
 */

public class OkHttpUtils {
    private static final String TAG = "OkHttpUtils";

    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtils(){
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }
    private synchronized static OkHttpUtils getmInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }
    private void getRequest(String url , final ResultCallback callback){
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);
    }
    private void postRequest(String url, final ResultCallback callback, List<Param> params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }
    private void deliveryResult(final ResultCallback resultCallback , Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailCallback(resultCallback, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                if(resultCallback.mType == String.class){
                    sendSuccessCallBack(resultCallback , str);
                }else {
                    Object object = JsonUtils.deserialize(str, resultCallback.mType);
                    sendSuccessCallBack(resultCallback, object);
                }
            }
        });
    }
    private void sendFailCallback(final ResultCallback resultCallback , final Exception e){
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (resultCallback != null) {
                    resultCallback.onFailure(e);
                }
            }
        });
    }
    private void sendSuccessCallBack(final ResultCallback resultCallback , final Object object){
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if(resultCallback != null){
                    resultCallback.onSuccess(object);
                }
            }
        });
    }
    private Request buildPostRequest(String url , List<Param> params){
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }
    /**********************对外接口************************/

    /**
     * get请求
     * @param url  请求url
     * @param callback  请求回调
     */
    public static void get(String url, ResultCallback callback) {
        getmInstance().getRequest(url, callback);
    }

    /**
     * post请求
     * @param url       请求url
     * @param callback  请求回调
     * @param params    请求参数
     */
    public static void post(String url, final ResultCallback callback, List<Param> params) {
        getmInstance().postRequest(url, callback, params);
    }

    public static abstract class ResultCallback<T> {

        Type mType;

        public ResultCallback(){
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         * @param response
         */
        public abstract void onSuccess(T response);

        /**
         * 请求失败回调
         * @param e
         */
        public abstract void onFailure(Exception e);
    }
    public static class Param {

        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }

}
