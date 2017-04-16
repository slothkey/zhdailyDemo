package com.shulan.simplegank.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shulan.simplegank.R;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houna on 17/4/14.
 */
public class TopVpAdapter extends PagerAdapter{

    private ArrayList<View> views;
    private List<ZhiHuTopStory> topList;

    public TopVpAdapter(ArrayList<View> views, List<ZhiHuTopStory> topList){
        this.views = views;
        this.topList = topList;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        Glide.with(container.getContext()).load(topList.get(position).getImage()).into(iv);
        tv.setText(topList.get(position).getTitle());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
