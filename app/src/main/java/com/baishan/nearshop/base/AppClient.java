package com.baishan.nearshop.base;


import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.nearshop.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 *
 */
public class AppClient {

    public static Retrofit mRetrofit;

    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (BuildConfig.DEBUG) {
                // Log信息拦截器
//                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//                    @Override
//                    public void log(String message) {
//                        Logger.i("RxJava", message);
//                    }
//                });
                okhttp3.logging.HttpLoggingInterceptor loggingInterceptor = new okhttp3.logging.HttpLoggingInterceptor();
                loggingInterceptor.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);
            }
//            builder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
//                    String timestamp = (System.currentTimeMillis() / 1000) + "";
////"U2FsdGVkX1oxBpC6PWpiVLbmWMbLF"
//                    String fixKey = md5(md5(md5("U2FsdGVkX1oxBpC6PWpiVLbmWMbLF").toLowerCase() + timestamp).toLowerCase()).toLowerCase();
//                    String userKey = "";
//                    if (userInfo != null) {
//                        userKey = md5(md5(md5(userInfo.user_token).toLowerCase() + userInfo.user_token.split("-")[0]).toLowerCase()).toLowerCase();
//                    }
//
//                    String key = timestamp + "|" + fixKey + "|" + userKey;
//                    Logger.i(key);
//                    String params = CommonUtil.encodeBase64(key);
//                    Logger.i("base64-" + params);
//
//
//                    try {
//                        Request.Builder builder = chain.request().newBuilder();
//                        builder.addHeader("Param-T",timestamp);
//                        builder.addHeader("Param-B",fixKey);
//                        builder.addHeader("Param-U",userKey);
////                        String value = new String(RSAUtils.encryptData(key.getBytes(), RSAUtils.loadPublicKey(ConstantValue.PUBLIC_RSA_KEY)));
//                        return chain.proceed(builder.build());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            });

//            builder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request();
//                    if (request.body() != null) {
//                        Buffer buffer = new Buffer();
//                        request.body().writeTo(buffer);
//                        Logger.i(buffer.readString(Charset.forName("UTF-8")));
//                    }
//
//                    return chain.proceed(request);
//                }
//            });

            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.API_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }

    public static ApiService getApiService() {
        return retrofit().create(ApiService.class);
    }

    public static String md5(String md5) {
        return CommonUtil.md5(md5);
    }

}
