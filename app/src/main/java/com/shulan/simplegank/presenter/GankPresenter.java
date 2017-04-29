package com.shulan.simplegank.presenter;

import com.shulan.simplegank.model.service.GankService;
import com.shulan.simplegank.model.theme.ThemeObject;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;
import com.shulan.simplegank.network.Network;
import com.shulan.simplegank.ui.IView.IGankView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by houna on 17/4/10.
 */
public class GankPresenter {

    private IGankView view;
    private List<ZhiHuStory> dataList;
    private List<ZhiHuTopStory> topList;
    private int beforeDays = 0;
    private long lastTime = 0;

    public GankPresenter(IGankView view){
        this.view = view;
    }

    public void getThemes() {
        Network.getManager()
                .create(GankService.class)
                .getThemes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ThemeObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ThemeObject value) {
                        view.refreshThemes(value);
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
