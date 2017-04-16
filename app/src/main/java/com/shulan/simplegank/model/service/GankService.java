package com.shulan.simplegank.model.service;

import com.shulan.simplegank.model.zhihu.ZhiHuDaily;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by houna on 17/4/10.
 */
public interface GankService {

    //获取最新的日报
    @GET("news/latest")
    Observable<ZhiHuDaily> latestNews();

    @GET("news/before/{beforeDays}")
    Observable<ZhiHuDaily> beforeNews(@Path("beforeDays")String beforDays);


}
