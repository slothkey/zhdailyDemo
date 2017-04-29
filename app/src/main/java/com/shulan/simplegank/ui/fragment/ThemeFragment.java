package com.shulan.simplegank.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shulan.simplegank.R;
import com.shulan.simplegank.adapter.ThemeAdapter;
import com.shulan.simplegank.model.theme.ThemeDetail;
import com.shulan.simplegank.presenter.ThemePresenter;
import com.shulan.simplegank.ui.IView.IThemeView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by houna on 17/4/18.
 */
// todo houna 123456
public class ThemeFragment extends Fragment implements IThemeView, SwipeRefreshLayout.OnRefreshListener {

    private String id; // 目前是想把theme name 作为每个fragment的tag的
    private RecyclerView rv;
    private SwipeRefreshLayout swipeLayout;
    private Context context;
    private ThemePresenter presenter;
    private ThemeAdapter adapter;
    private boolean isRefresh;

    public static ThemeFragment newInstance(String id){
        Bundle args = new Bundle();
        ThemeFragment fragment = new ThemeFragment();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);

        swipeLayout.setDistanceToTriggerSync(300);
        swipeLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initRv();
        presenter = new ThemePresenter(this);
        presenter.refreshTheme(id);
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ThemeAdapter(context);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(!rv.canScrollVertically(1) && newState == SCROLL_STATE_IDLE){
                    if(adapter.getStories().size() == 0){
                        return;
                    }
                    presenter.loadThemes(id, adapter.getLastId());
                    // http://news-at.zhihu.com/api/4/theme/11/before/7049075 这是请求的网址

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    @Override
    public void refreshTheme(ThemeDetail value) {
        if(isRefresh){
            isRefresh = false;
            swipeLayout.setRefreshing(false);
        }
        adapter.setData(value);
    }

    @Override
    public void loadMoreThemes(ThemeDetail value) {
        adapter.setDataStories(value.getStories());
    }

    @Override
    public void onRefresh() {
        if(!isRefresh){
            isRefresh = true;
            presenter.refreshTheme(id);
        }
    }
}
