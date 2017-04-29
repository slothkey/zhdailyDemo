package com.shulan.simplegank.model.service;

import com.shulan.simplegank.model.CommentObj;
import com.shulan.simplegank.model.detail.StoryDetail;
import com.shulan.simplegank.model.detail.StoryExtra;
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

    // 单个story的具体详情
    @GET("story/{id}")
    Observable<StoryDetail> getStory(@Path("id")String id);

    @GET("story-extra/{id}")
    Observable<StoryExtra> getStoryExtra(@Path("id")String id);

    @GET("story/{id}/long-comments")
    Observable<CommentObj> getLongComments(@Path("id")String id);

    @GET("story/{id}/short-comments")
    Observable<CommentObj> getShortComments(@Path("id")String id);

    @GET("story/{id}/long-comments/before/{storyId}")
    Observable<CommentObj> getMoreLongComments(@Path("id")String id, @Path("storyId")int storyId);

    @GET("story/{id}/short-comments/before/{storyId}")
    Observable<CommentObj> getMoreShortComments(@Path("id")String id, @Path("storyId")int storyId);

}
