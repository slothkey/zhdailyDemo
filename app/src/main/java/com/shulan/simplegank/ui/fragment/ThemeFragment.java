package com.shulan.simplegank.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shulan.simplegank.R;

/**
 * Created by houna on 17/4/18.
 */

public class ThemeFragment extends Fragment {

    private String tag; // 目前是想把theme name 作为每个fragment的tag的
    private RecyclerView rv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        initRv();
        return view;
    }

    private void initRv() {

    }


}
