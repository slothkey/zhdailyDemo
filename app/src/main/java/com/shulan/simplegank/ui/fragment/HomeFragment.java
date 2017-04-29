package com.shulan.simplegank.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shulan.simplegank.R;
import com.shulan.simplegank.adapter.GankAdapter;
import com.shulan.simplegank.event.HomeTitleChangeEvent;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;
import com.shulan.simplegank.presenter.HomePresenter;
import com.shulan.simplegank.ui.IView.IHomeView;
import com.shulan.simplegank.ui.common.WebActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by houna on 17/4/18.
 */

public class HomeFragment extends Fragment implements IHomeView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rv;
    private SwipeRefreshLayout swipeLayout;
    private GankAdapter adapter;
    private Context context;
    private HomePresenter presenter;
    private LinearLayoutManager manager;
    private boolean isRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);

        swipeLayout.setDistanceToTriggerSync(300);
        swipeLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeLayout.setOnRefreshListener(this);

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
        manager = new LinearLayoutManager(context);
        rv.setLayoutManager(manager);
        adapter = new GankAdapter(context);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(!rv.canScrollVertically(1)&& newState == SCROLL_STATE_IDLE){
//                    logE("scroll", "滑到底部了");
                    presenter.loadGanks();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = manager.findFirstVisibleItemPosition();
                String title = adapter.getTitle(position);
                if(!TextUtils.isEmpty(title)){
                    // todo 发event
                    EventBus.getDefault().post(new HomeTitleChangeEvent(title));
                }
            }
        });


    }

    public View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(WebActivity.newInstance(context,  String.valueOf(v.getTag())));
        }
    };

    @Override
    public void refreshSuccess(List<ZhiHuStory> dataList, List<ZhiHuTopStory> topList) {
        if(isRefresh){
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }
        adapter.setDataList(dataList, topList);
    }

    @Override
    public void onRefresh() {
        if(!isRefresh){
            isRefresh = true;
            presenter.refreshGank();
        }

    }
}
