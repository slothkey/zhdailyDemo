package com.shulan.simplegank.event;

/**
 * Created by houna on 17/4/20.
 */

public class HomeTitleChangeEvent {

    private String title;

    public HomeTitleChangeEvent(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
