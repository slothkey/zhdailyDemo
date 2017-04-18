package com.shulan.simplegank.presenter;

import com.shulan.simplegank.R;
import com.shulan.simplegank.model.service.GankService;
import com.shulan.simplegank.model.theme.ThemeObject;
import com.shulan.simplegank.model.zhihu.ZhiHuDaily;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;
import com.shulan.simplegank.network.Network;
import com.shulan.simplegank.ui.IView.IGankView;
import com.shulan.simplegank.utils.TimeUtils;

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

    public void refreshGank(){
        Network.getManager()
                .create(GankService.class)
                .latestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDaily>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ZhiHuDaily value) {
                        dataList = value.getStories();
                        topList = value.getTop_stories();
                        dataList.get(0).setDate(view.getContext().getResources().getString(R.string.today_news));
                        view.refreshSuccess(dataList, topList);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadGanks(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastTime < 500){
            return;
        }
        lastTime = currentTime;
        Network.getManager()
                .create(GankService.class)
                .beforeNews(TimeUtils.networkDate(beforeDays++))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDaily>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ZhiHuDaily value) {
                        // TODO 改天做个时间的显示的转换
                        value.getStories().get(0).setDate(value.getDate());
                        dataList.addAll(value.getStories());
                        view.refreshSuccess(dataList, topList); // todo 之后这里可以提出来 updateUI()

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

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
