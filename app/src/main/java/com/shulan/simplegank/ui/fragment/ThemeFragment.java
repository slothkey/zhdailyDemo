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
import com.shulan.simplegank.adapter.ThemeAdapter;
import com.shulan.simplegank.model.theme.ThemeDetail;
import com.shulan.simplegank.presenter.ThemePresenter;
import com.shulan.simplegank.ui.IView.IThemeView;

/**
 * Created by houna on 17/4/18.
 */
// todo houna 123456
public class ThemeFragment extends Fragment implements IThemeView {

    private String id; // 目前是想把theme name 作为每个fragment的tag的
    private RecyclerView rv;
    private Context context;
    private ThemePresenter presenter;
    private ThemeAdapter adapter;

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
                if(!rv.canScrollVertically(1)){
                    presenter.loadThemes(id, adapter.getLastId());
                    // todo  http://news-at.zhihu.com/api/4/theme/11/before/7049075 这是请求的网址

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
        adapter.setData(value);
    }

    @Override
    public void loadMoreThemes(ThemeDetail value) {
        adapter.setDataStories(value.getStories());
    }

}
