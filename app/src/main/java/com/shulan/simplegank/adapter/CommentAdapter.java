package com.shulan.simplegank.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shulan.simplegank.R;
import com.shulan.simplegank.event.UnfoldShortCommentEvent;
import com.shulan.simplegank.model.Comment;
import com.shulan.simplegank.model.detail.StoryExtra;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houna on 17/4/20.
 *
 * // 一 目前看到的展示评论页面规则是：
 // 1 长评是不作为一个条目出现，而是具体的条目的一部分
 // 2 如果长评有
 // 0条： 有一个空页面（也就是说，刚进去有两个条目）
 // 任何其他条：刚进去有n+1条（短评默认收起列表）
 // 3 如果点击短评的展开，则展开显示
 // 4 点击短评展开，就会这一条滑到最上面
 // （而且是这时候才开始加载短评）
 *
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

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
    private StoryExtra storyExtra;

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

    public boolean isFold() {
        return isFold;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_LONG_EMPTY || viewType == TYPE_LONG_HEAD || viewType == TYPE_LONG_NORMAL){
            return new LongCommentHolder(LayoutInflater.from(context).inflate(R.layout.comment_long, parent, false));
        }
        return new ShortCommentHolder(LayoutInflater.from(context).inflate(R.layout.comment_short, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        // 可以认为如果是相应的type，那么就一定是 LongCommentHolder 或者是 ShortCommentHolder
        switch (getItemViewType(position)){
            case TYPE_LONG_EMPTY:
                holder.updateUI(TYPE_LONG_EMPTY, null);
                break;
            case TYPE_LONG_HEAD:
                holder.updateUI(TYPE_LONG_HEAD, longComments.get(0));
                break;
            case TYPE_LONG_NORMAL:
                holder.updateUI(TYPE_LONG_NORMAL, longComments.get(position));
                break;
            case TYPE_SHORT_HEAD_FOLD:
                holder.updateUI(TYPE_SHORT_HEAD_FOLD, null);
                break;
            case TYPE_SHORT_HEAD_UNFOLD:
                holder.updateUI(TYPE_SHORT_HEAD_UNFOLD, shortComments.get(position - Math.max(1, longComments.size())));
                break;
            case TYPE_SHORT_NORMAL:
                holder.updateUI(TYPE_SHORT_NORMAL, shortComments.get(position - Math.max(1, longComments.size())));
                break;
        }

    }

    private CharSequence getSpanReply(Comment.ReplyToBean reply) {
        if(TextUtils.isEmpty(reply.getAuthor()) || TextUtils.isEmpty(reply.getContent())){
            return context.getResources().getString(R.string.empty_reply_to);
        }
        String string = String.format(context.getResources().getString(R.string.reply_to), reply.getAuthor(), reply.getContent());
        SpannableString sString = new SpannableString(string);
        sString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, reply.getAuthor().length() + 3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sString.setSpan(new StyleSpan(Typeface.BOLD), 0, reply.getAuthor().length() + 3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return sString;
    }

    private View.OnClickListener onFoldListener;
    private View.OnClickListener getFoldListener() {
        if(onFoldListener == null){
            onFoldListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isFold = !isFold;
                    if(!isFold){
                        int position = Math.max(getLongComments().size(), 1);
                        EventBus.getDefault().post(new UnfoldShortCommentEvent(position));
                    }
                    notifyDataSetChanged();
                }
            };
        }
        return onFoldListener;
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
                return isFold ? TYPE_SHORT_HEAD_FOLD : (position == 1 ? TYPE_SHORT_HEAD_UNFOLD : TYPE_SHORT_NORMAL);
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
            return isFold ? TYPE_SHORT_HEAD_FOLD : (position == getLongComments().size() ? TYPE_SHORT_HEAD_UNFOLD : TYPE_SHORT_NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        if(getLongComments().size() == 0 && getShortComments().size() == 0){
            return 2;
        }else if(getLongComments().size() == 0){ // 说明此时 短评论数量一定不为0
            return 1 + (isFold ? 1 : getShortComments().size());
        }else{ // 说明此时 长评论数量一定不为0
            return getLongComments().size() + (isFold ? 1 : Math.max(1, getShortComments().size())); // 如果长评论为0，返回1；否则返回短评论数量
        }
    }

    public void setComments(List<Comment> longComments, List<Comment> shortComments) {
        this.longComments = longComments;
        this.shortComments = shortComments;
        notifyItemInserted(getItemCount());
    }

    public void setStoryExtra(StoryExtra storyExtra) {
        this.storyExtra = storyExtra;
    }

    public class CommentHolder extends RecyclerView.ViewHolder{

        private ImageView avatar;
        private TextView author;
        private TextView likes;
        private TextView content;
        private TextView time;
        private TextView fold;
        private TextView replyTo;

        public CommentHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            author = (TextView) itemView.findViewById(R.id.author);
            likes = (TextView) itemView.findViewById(R.id.likes);
            content = (TextView) itemView.findViewById(R.id.content);
            time = (TextView) itemView.findViewById(R.id.time);
            fold = (TextView) itemView.findViewById(R.id.fold);
            replyTo = (TextView) itemView.findViewById(R.id.reply_to);
        }

        public void updateUI(int type, final Comment comment){
            Glide.with(context).load(comment.getAvatar()).into(avatar);
            author.setText(comment.getAuthor());
            likes.setText(comment.getLikes() + "");
            content.setText(comment.getContent());
            time.setText(comment.getTime() + "");
            if(comment.getReply_to() == null){
                replyTo.setVisibility(View.GONE);
                fold.setVisibility(View.GONE);
            }else{
                replyTo.setVisibility(View.VISIBLE);
                if(replyTo.getLineCount() == 2){
                    replyTo.setMaxLines(Integer.MAX_VALUE);
                }
                replyTo.setText(getSpanReply(comment.getReply_to()));

                replyTo.setMaxLines(Integer.MAX_VALUE);
                replyTo.post(new Runnable() {
                    @Override
                    public void run() {
                        int count = replyTo.getLineCount();
                        Log.e("lineCount", comment.getId() + " " + count + "  ");
                        if(count > 2){
                            fold.setVisibility(View.VISIBLE);

                            if(comment.getReply_to().isFold()){
                                replyTo.setMaxLines(2);
                                fold.setText("展开");
                            }else{
                                replyTo.setMaxLines(Integer.MAX_VALUE);
                                fold.setText("收起");
                            }

                        }else{
                            fold.setVisibility(View.GONE);
                        }
                    }
                });

                fold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(comment.getReply_to().isFold()){
                            replyTo.setMaxLines(Integer.MAX_VALUE);
                            fold.setText("收起");
                            comment.getReply_to().setFold(false);
                        }else{
                            replyTo.setMaxLines(2);
                            fold.setText("展开");
                            comment.getReply_to().setFold(true);
                        }
                    }
                });

            }

        }
    }

    public class LongCommentHolder extends CommentHolder{

        private TextView head;
        private View empty;
        private View container;

        public LongCommentHolder(View itemView) {
            super(itemView);
            head = (TextView) itemView.findViewById(R.id.head);
            empty = itemView.findViewById(R.id.empty);
            container = itemView.findViewById(R.id.container);
        }

        public void updateUI(int type, Comment comment) {
            if(type == TYPE_LONG_EMPTY || type == TYPE_LONG_HEAD){
                head.setVisibility(View.VISIBLE);
                head.setText(String.format(context.getResources().getString(R.string.counts_long_comments), storyExtra.getLong_comments()));
                empty.setVisibility(View.VISIBLE);
                container.setVisibility(View.GONE);
            }else if(type == TYPE_LONG_NORMAL){
                head.setVisibility(View.GONE);
            }
            if(type == TYPE_LONG_EMPTY){
                empty.setVisibility(View.VISIBLE);
                container.setVisibility(View.GONE);
            }else if(type == TYPE_LONG_HEAD || type == TYPE_LONG_NORMAL){
                empty.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
                super.updateUI(type, comment);
            }

        }
    }

    public class ShortCommentHolder extends CommentHolder{

        private View head;
        private TextView headCount;
        private ImageView headFold;
        private View container;

        public ShortCommentHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.head);
            headCount = (TextView) itemView.findViewById(R.id.head_count);
            headFold = (ImageView) itemView.findViewById(R.id.head_fold);
            container = itemView.findViewById(R.id.container);
        }

        public void updateUI(int type, Comment comment) {
            if(type == TYPE_SHORT_HEAD_FOLD || type == TYPE_SHORT_HEAD_UNFOLD){
                head.setVisibility(View.VISIBLE);
                headCount.setText(String.format(context.getResources().getString(R.string.counts_short_comments), storyExtra.getShort_comments()));
                headFold.setImageResource(R.mipmap.comment_icon_fold);
                head.setOnClickListener(getFoldListener());
            }else if(type == TYPE_SHORT_NORMAL){
                head.setVisibility(View.GONE);
            }
            if(type == TYPE_SHORT_HEAD_FOLD){
                container.setVisibility(View.GONE);
            }else if(type == TYPE_SHORT_HEAD_UNFOLD || type == TYPE_SHORT_NORMAL){
                container.setVisibility(View.VISIBLE);
                super.updateUI(type, comment);
            }

        }
    }

}
