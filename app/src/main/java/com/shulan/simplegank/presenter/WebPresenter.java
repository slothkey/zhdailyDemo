package com.shulan.simplegank.presenter;

import android.content.Intent;

import com.shulan.simplegank.model.detail.StoryDetail;
import com.shulan.simplegank.model.detail.StoryExtra;
import com.shulan.simplegank.model.service.GankService;
import com.shulan.simplegank.network.Network;
import com.shulan.simplegank.ui.IView.IWebView;
import com.shulan.simplegank.ui.common.WebActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by houna on 17/4/19.
 */

public class WebPresenter {

    private String storyId;
    private IWebView view;
    private StoryExtra storyExtra;

    public WebPresenter(IWebView view, Intent intent) {
        this.view = view;
        storyId = intent.getStringExtra(WebActivity.PARAMS_STORY_ID);
    }

    public String getStoryId(){
        return storyId;
    }

    public void getStoryDetail(){
        Network.getManager()
                .create(GankService.class)
                .getStory(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StoryDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StoryDetail value) {
                        view.refreshUI(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getStoryExtra(){
        Network.getManager()
                .create(GankService.class)
                .getStoryExtra(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StoryExtra>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StoryExtra value) {
                        view.refreshBar(value);
                        storyExtra = value;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public StoryExtra getStoryExtraInfo(){
        return storyExtra;
    }
}
