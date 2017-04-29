package com.shulan.simplegank.event;

/**
 * Created by houna on 17/4/26.
 */

public class UnfoldShortCommentEvent {

    private int position;

    public UnfoldShortCommentEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
