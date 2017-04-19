package com.shulan.simplegank.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shulan.simplegank.R;
import com.shulan.simplegank.adapter.GankAdapter;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;
import com.shulan.simplegank.presenter.HomePresenter;
import com.shulan.simplegank.ui.IView.IHomeView;

import java.util.List;

/**
 * Created by houna on 17/4/18.
 */

public class HomeFragment extends Fragment implements IHomeView {

    private RecyclerView rv;
    private GankAdapter adapter;
    private Context context;
    private HomePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initRv();
        presenter = new HomePresenter(this);
        presenter.refreshGank();
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new GankAdapter(context);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(!rv.canScrollVertically(1)){
//                    logE("scroll", "滑到底部了");
                    presenter.loadGanks();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    public void refreshSuccess(List<ZhiHuStory> dataList, List<ZhiHuTopStory> topList) {
        adapter.setDataList(dataList, topList);
    }
}
