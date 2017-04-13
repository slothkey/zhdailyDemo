package com.shulan.hn.simplegank.model.service;

import com.shulan.hn.simplegank.model.Info;
import com.shulan.hn.simplegank.model.zhihu.ZhiHuDaily;


import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by houna on 17/4/10.
 */
public interface GankService {

    //获取最近的日报
    @GET("news/latest")
    Observable<ZhiHuDaily> gankList();

}
