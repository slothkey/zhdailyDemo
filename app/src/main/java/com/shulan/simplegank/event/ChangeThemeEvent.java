package com.shulan.simplegank.event;

/**
 * Created by houna on 17/4/19.
 */

public class ChangeThemeEvent {

    private String id;

    public ChangeThemeEvent(String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
