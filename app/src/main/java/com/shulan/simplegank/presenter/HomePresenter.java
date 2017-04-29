package com.shulan.simplegank.presenter;

import com.shulan.simplegank.R;
import com.shulan.simplegank.model.service.GankService;
import com.shulan.simplegank.model.zhihu.ZhiHuDaily;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;
import com.shulan.simplegank.network.Network;
import com.shulan.simplegank.ui.IView.IHomeView;
import com.shulan.simplegank.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by houna on 17/4/18.
 */

public class HomePresenter {

    private IHomeView view;
    private List<ZhiHuStory> dataList;
    private List<ZhiHuTopStory> topList;
    private int beforeDays = 0;
    private long lastTime = 0;

    public HomePresenter(IHomeView view){
        this.view = view;
        dataList = new ArrayList<>();
        topList = new ArrayList<>();
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
                        dataList.clear();
                        dataList.addAll(value.getStories());
                        topList.clear();
                        topList.addAll(value.getTop_stories());
                        dataList.get(0).setDate(view.getActivity().getResources().getString(R.string.today_news));
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
                        value.getStories().get(0).setDate(TimeUtils.getMDWeek(value.getDate()));
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


}
