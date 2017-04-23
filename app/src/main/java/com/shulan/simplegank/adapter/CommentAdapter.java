package com.shulan.simplegank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shulan.simplegank.R;
import com.shulan.simplegank.model.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houna on 17/4/20.
 */

public class CommentAdapter extends RecyclerView.Adapter {

    private final int TYPE_LONG_EMPTY = 101;    // 空页面
    private final int TYPE_LONG_HEAD = 102;     // 带头的
    private final int TYPE_LONG_NORMAL = 103;   // 普通的
    private final int TYPE_SHORT_HEAD_FOLD = 201;   // 带头的（折叠的）
    private final int TYPE_SHORT_HEAD_UNFOLD = 202; // 带头的（展开的）
    private final int TYPE_SHORT_NORMAL = 203;  // 普通的


    private Context context;
    private List<Comment> longComments;
    private List<Comment> shortComments;
    private boolean isFold = true; // 默认是折叠的

    public CommentAdapter(Context context){
        this.context = context;
    }

    public List<Comment> getLongComments(){
        if(longComments == null){
            longComments = new ArrayList<>();
        }
        return longComments;
    }

    public List<Comment> getShortComments(){
        if(shortComments == null){
            shortComments = new ArrayList<>();
        }
        return shortComments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_LONG_EMPTY || viewType == TYPE_LONG_HEAD || viewType == TYPE_LONG_NORMAL){
            return new LongCommentHolder(LayoutInflater.from(context).inflate(R.layout.comment_long, parent, false));
        }
        return new ShortCommentHolder(LayoutInflater.from(context).inflate(R.layout.comment_short, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 可以认为如果是相应的type，那么就一定是 LongCommentHolder 或者是 ShortCommentHolder
        Comment comment;
        switch (getItemViewType(position)){
            case TYPE_LONG_EMPTY:
                LongCommentHolder longHolder = (LongCommentHolder) holder;
                longHolder.head.setVisibility(View.VISIBLE);
                longHolder.empty.setVisibility(View.VISIBLE);
                longHolder.container.setVisibility(View.GONE);
                longHolder.head.setText(String.format(context.getResources().getString(R.string.counts_long_comments), longComments.size())); // todo 其实之后不能这么做，要传递过来的，现在为了方便
                break;
            case TYPE_LONG_HEAD:
                longHolder = (LongCommentHolder) holder;
                longHolder.head.setVisibility(View.VISIBLE);
                longHolder.empty.setVisibility(View.GONE);
                longHolder.container.setVisibility(View.VISIBLE);
                longHolder.head.setText(String.format(context.getResources().getString(R.string.counts_long_comments), longComments.size()));
                comment = longComments.get(0);
                Glide.with(context).load(comment.getAvatar()).into(longHolder.avatar);
                longHolder.author.setText(comment.getAuthor());
                longHolder.likes.setText(comment.getLikes() + "");
                longHolder.content.setText(comment.getContent());
                longHolder.time.setText(comment.getTime() + "");
//                longHolder.fold.setText(comment.ge);
                break;
            case TYPE_LONG_NORMAL:
                longHolder = (LongCommentHolder) holder;
                longHolder.head.setVisibility(View.GONE);
                longHolder.empty.setVisibility(View.GONE);
                longHolder.container.setVisibility(View.VISIBLE);
                comment = longComments.get(position);
                Glide.with(context).load(comment.getAvatar()).into(longHolder.avatar);
                longHolder.author.setText(comment.getAuthor());
                longHolder.likes.setText(comment.getLikes() + "");
                longHolder.content.setText(comment.getContent());
                longHolder.time.setText(comment.getTime() + "");
                break;
            case TYPE_SHORT_HEAD_FOLD:
                ShortCommentHolder shortHolder = (ShortCommentHolder) holder;
                shortHolder.head.setVisibility(View.VISIBLE);
                shortHolder.container.setVisibility(View.GONE);
                break;
            case TYPE_SHORT_HEAD_UNFOLD:
                shortHolder = (ShortCommentHolder) holder;
                shortHolder.head.setVisibility(View.VISIBLE);
                shortHolder.container.setVisibility(View.VISIBLE);
                break;
            case TYPE_SHORT_NORMAL:
                shortHolder = (ShortCommentHolder) holder;
                shortHolder.head.setVisibility(View.GONE);
                shortHolder.container.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(getLongComments().size() == 0 && getShortComments().size() == 0){
            if(position == 0){
                return  TYPE_LONG_EMPTY;
            }else{
                return TYPE_SHORT_HEAD_FOLD;
            }
        }else if(getLongComments().size() == 0){ // 说明此时 短评论数量一定不为0
            if(position == 0){
                return TYPE_LONG_EMPTY;
            }else {
                return TYPE_SHORT_HEAD_FOLD; // todo 这里之后可能会改变
            }
        }
        // 说明此时 长评论数量一定不为0
        if(position < getLongComments().size()){
            if(position == 0){
                return TYPE_LONG_HEAD;
            }else{
                return TYPE_LONG_NORMAL;
            }
        }else{
            return TYPE_SHORT_HEAD_FOLD; // todo 这里可能还要改
        }
    }

    @Override
    public int getItemCount() {
        if(getLongComments().size() == 0 && getShortComments().size() == 0){
            return 2;
        }else if(getLongComments().size() == 0){ // 说明此时 短评论数量一定不为0
            return 1 + getShortComments().size();
        }else{ // 说明此时 长评论数量一定不为0
            return getLongComments().size() + Math.max(1, getShortComments().size()); // 如果长评论为0，返回1；否则返回短评论数量
        }
    }

    public void setComments(List<Comment> longComments, List<Comment> shortComments) {
        this.longComments = longComments;
        this.shortComments = shortComments;
        notifyDataSetChanged();
    }

    public class LongCommentHolder extends RecyclerView.ViewHolder{

        private TextView head;
        private View empty;
        private View container;
        private ImageView avatar;
        private TextView author;
        private TextView likes;
        private TextView content;
        private TextView time;
        private TextView fold;

        public LongCommentHolder(View itemView) {
            super(itemView);
            head = (TextView) itemView.findViewById(R.id.head);
            empty = itemView.findViewById(R.id.empty);
            container = itemView.findViewById(R.id.container);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            author = (TextView) itemView.findViewById(R.id.author);
            likes = (TextView) itemView.findViewById(R.id.likes);
            content = (TextView) itemView.findViewById(R.id.content);
            time = (TextView) itemView.findViewById(R.id.time);
            fold = (TextView) itemView.findViewById(R.id.fold);
        }
    }

    public class ShortCommentHolder extends RecyclerView.ViewHolder{

        private TextView head;
        private View container;

        public ShortCommentHolder(View itemView) {
            super(itemView);
            head = (TextView) itemView.findViewById(R.id.head);
            container = itemView.findViewById(R.id.container);
        }
    }

}
