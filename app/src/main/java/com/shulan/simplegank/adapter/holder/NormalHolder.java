package com.shulan.simplegank.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shulan.simplegank.R;

/**
 * Created by houna on 17/4/19.
 */

public class NormalHolder extends RecyclerView.ViewHolder {
    public TextView date;
    public TextView title;
    public ImageView img;

    public NormalHolder(View view) {
        super(view);
        date = (TextView) view.findViewById(R.id.date);
        title = (TextView) view.findViewById(R.id.title);
        img = (ImageView) view.findViewById(R.id.img);
    }
}