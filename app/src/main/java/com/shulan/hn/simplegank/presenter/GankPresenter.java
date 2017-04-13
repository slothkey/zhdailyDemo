package com.shulan.hn.simplegank.presenter;

import android.util.Log;

import com.shulan.hn.simplegank.model.Config;
import com.shulan.hn.simplegank.model.Info;
import com.shulan.hn.simplegank.model.service.GankService;
import com.shulan.hn.simplegank.model.zhihu.ZhiHuDaily;
import com.shulan.hn.simplegank.ui.IView.IGankView;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by houna on 17/4/10.
 */
public class GankPresenter {

    private IGankView view;

    public GankPresenter(IGankView view){
        this.view = view;
    }

    public void refreshGank(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        // 目前log 是 debug OkHttp 就可以看了
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Config.ZHIHU_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GankService service = retrofit.create(GankService.class);
        service.gankList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDaily>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ZhiHuDaily value) {
                        view.refreshSuccess(value);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
