package com.shulan.simplegank.presenter;

import android.util.Log;

import com.shulan.simplegank.model.service.GankService;
import com.shulan.simplegank.model.theme.ThemeDetail;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.network.Network;
import com.shulan.simplegank.ui.IView.IThemeView;
import com.shulan.simplegank.utils.SpUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by houna on 17/4/18.
 */

public class ThemePresenter {

    private IThemeView view;

    public ThemePresenter(IThemeView view) {
        this.view = view;
    }


    public void refreshTheme(final String id) {
        Network.getManager()
                .create(GankService.class)
                .getThemeDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ThemeDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("network", "onSubscribe");
                    }

                    @Override
                    public void onNext(ThemeDetail value) {
                        Log.e("network", "onNext");
                        List<ZhiHuStory> stories = value.getStories();
                        for(int i = 0; i < stories.size(); i++){
                            stories.get(i).setReaded(SpUtils.isReaded(SpUtils.THEME + id, stories.get(i).getId()));
                        }
                        view.refreshTheme(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("network", "onError");
                        view.refreshTheme(new ThemeDetail());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("network", "onComplete");
                    }
                });

    }

    public void loadThemes(final String id, String storyId) {
        Network.getManager()
                .create(GankService.class)
                .beforeThemeDetail(id, storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ThemeDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ThemeDetail value) {
                        List<ZhiHuStory> stories = value.getStories();
                        for(int i = 0; i < stories.size(); i++){
                            stories.get(i).setReaded(SpUtils.isReaded(SpUtils.THEME + id, stories.get(i).getId()));
                        }
                        view.loadMoreThemes(value);
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
