package com.shulan.simplegank.network;

import com.shulan.simplegank.config.App;
import com.shulan.simplegank.model.Config;
import com.shulan.simplegank.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by houna on 17/4/15.
 */
public class Network {

    public static Retrofit getManager() {
        initOkHttpClient();  // TODO 他那边是用的非静态的方法的

        // 目前log 是 debug OkHttp 就可以看了
        Retrofit retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(Config.ZHIHU_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    //长缓存有效期为7天
    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;

    private static OkHttpClient mOkHttpClient;

    private static void initOkHttpClient(){
        if(mOkHttpClient == null){
            synchronized (Network.class){
                if(mOkHttpClient == null){
                    Cache cache = new Cache(new File(App.getContext().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)     // 错误重联 (是否自动重连)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    // http://blog.csdn.net/picasso_l/article/details/50579884 *** todo  这个特别好，以后一定要看
    // http://blog.csdn.net/adzcsx2/article/details/51365548 *** todo 这个也要看
    private static Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(!NetUtils.isNetworkConnected()){
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response originalResponse = chain.proceed(request);
            if(NetUtils.isNetworkConnected()){
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder().
                        header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
            }else{
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)   //设置缓存策略，及超时策略
                        .removeHeader("Pragma").build();    //清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
            }
        }
    };


}
