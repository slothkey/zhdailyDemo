package com.shulan.simplegank.adapter;

/**
 * Created by houna on 17/4/10.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shulan.simplegank.R;
import com.shulan.simplegank.model.zhihu.ZhiHuStory;
import com.shulan.simplegank.model.zhihu.ZhiHuTopStory;

import java.util.ArrayList;
import java.util.List;


public class GankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_TOP = 1;
    private final int TYPE_NORMAL = 2;

    private Context context;
    private List<ZhiHuStory> dataList;
    private List<ZhiHuTopStory> topList;

    public GankAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<>();
    }

    public void setDataList(List<ZhiHuStory> dataList, List<ZhiHuTopStory> topList) {
        this.dataList = dataList;
        this.topList = topList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_TOP){
            return new TopHolder(LayoutInflater.from(context).inflate(R.layout.item_top_gank, parent, false));
        }
        return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.item_gank, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NormalHolder){
            ZhiHuStory data = dataList.get(position - 1);
            NormalHolder normalHolder = (NormalHolder) holder;
            if(TextUtils.isEmpty(data.getDate())){
                normalHolder.date.setVisibility(View.GONE);
            }else{
                normalHolder.date.setVisibility(View.VISIBLE);
                normalHolder.date.setText(data.getDate());
            }
            normalHolder.title.setText(data.getTitle());
            List<String> images = data.getImages();
            if(images != null && images.size() > 0){
                normalHolder.img.setVisibility(View.VISIBLE);
                Glide.with(context).load(images.get(0)).into(normalHolder.img);
            }else{
                normalHolder.img.setVisibility(View.GONE);
            }
        }else if(holder instanceof TopHolder){
            TopHolder topHolder = (TopHolder) holder;
            ArrayList<View> views = new ArrayList<>();
            for(int i = 0; i < topList.size(); i++){
                views.add(getImageView());
            }
            topHolder.vp.setAdapter(new TopVpAdapter(views, topList));
        }

    }

    private View getImageView() {
        return LayoutInflater.from(context).inflate(R.layout.item_vp_top, null, false);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_TOP : TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (dataList == null || dataList.size() == 0) {
            return 0;
        }
        return dataList.size() + 1;
    }



    class NormalHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView title;
        ImageView img;

        NormalHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            title = (TextView) view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }

    class TopHolder extends RecyclerView.ViewHolder{

        private ViewPager vp;

        public TopHolder(View itemView) {
            super(itemView);
            vp = (ViewPager) itemView.findViewById(R.id.vp);
        }
    }
}
