package com.shulan.hn.simplegank.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shulan.hn.simplegank.R;
import com.shulan.hn.simplegank.adapter.GankAdapter;
import com.shulan.hn.simplegank.model.zhihu.ZhiHuDaily;
import com.shulan.hn.simplegank.presenter.GankPresenter;
import com.shulan.hn.simplegank.ui.IView.IGankView;

public class MainActivity extends AppCompatActivity implements IGankView {

    private RecyclerView rv;
    private GankAdapter adapter;
    private GankPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRv();
        presenter = new GankPresenter(this);
        presenter.refreshGank();
    }

    private void initRv() {
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new GankAdapter(this);
        rv.setAdapter(adapter);
    }

    @Override
    public void refreshSuccess(ZhiHuDaily zhiHuDaily) {
        adapter.setDataList(zhiHuDaily.getStories());
    }
}
