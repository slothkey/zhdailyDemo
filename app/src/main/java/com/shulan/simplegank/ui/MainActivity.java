package com.shulan.simplegank.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shulan.simplegank.R;
import com.shulan.simplegank.adapter.GankAdapter;
import com.shulan.simplegank.base.BaseActivity;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;
import com.shulan.simplegank.presenter.GankPresenter;
import com.shulan.simplegank.ui.IView.IGankView;

import java.util.List;

public class MainActivity extends BaseActivity implements IGankView {

    private RecyclerView rv;
    private GankAdapter adapter;
    private GankPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.syncState();
        drawerLayout.setDrawerListener(toggle);

        initRv();
        presenter = new GankPresenter(this);
        presenter.refreshGank();
    }

    private void initRv() {
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GankAdapter(this);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(!rv.canScrollVertically(1)){
                    logE("scroll", "滑到底部了");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // todo
        switch (item.getItemId()){
            case R.id.btn_share:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshSuccess(List<ZhiHuStory> dataList, List<ZhiHuTopStory> topList) {
        adapter.setDataList(dataList, topList);
    }
}
