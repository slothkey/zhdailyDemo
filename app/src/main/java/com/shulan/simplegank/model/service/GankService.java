package com.shulan.simplegank.model.service;

import com.shulan.simplegank.model.theme.ThemeDetail;
import com.shulan.simplegank.model.theme.ThemeObject;
import com.shulan.simplegank.model.zhihu.ZhiHuDaily;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by houna on 17/4/10.
 */
public interface GankService {

    // 获取最新的日报
    @GET("news/latest")
    Observable<ZhiHuDaily> latestNews();

    // 过往消息
    @GET("news/before/{beforeDays}")
    Observable<ZhiHuDaily> beforeNews(@Path("beforeDays")String beforDays);

    // 主题日报列表查看
    @GET("themes")
    Observable<ThemeObject> getThemes();

    @GET("theme/{id}")
    Observable<ThemeDetail> getThemeDetail(@Path("id")String id);

    @GET("theme/{id}/before/{storyId}")
    Observable<ThemeDetail> beforeThemeDetail(@Path("id")String id, @Path("storyId")String storyId);

}
