package com.shulan.hn.simplegank.adapter;

/**
 * Created by houna on 17/4/10.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shulan.hn.simplegank.R;
import com.shulan.hn.simplegank.model.zhihu.ZhiHuStory;

import java.util.ArrayList;
import java.util.List;


public class GankAdapter extends RecyclerView.Adapter<GankAdapter.Holder> {

    private Context context;
    private List<ZhiHuStory> dataList;

    public GankAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<>();
    }

    public void setDataList(ArrayList<ZhiHuStory> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public GankAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_gank, parent, false));
    }

    @Override
    public void onBindViewHolder(GankAdapter.Holder holder, int position) {
        final ZhiHuStory data = dataList.get(position);
        holder.tv.setText(data.getTitle());

    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tv;

        Holder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
        }
    }
}

