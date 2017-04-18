package com.shulan.simplegank.model.theme;

/**
 * Created by houna on 17/4/18.
 */

public class Editor {
    /**
     * url : http://www.zhihu.com/people/THANKS
     * bio : FreeBuf.com 小编，专注黑客与极客
     * id : 65
     * avatar : http://pic4.zhimg.com/ecd93e213_m.jpg
     * name : THANKS
     */

    private String url;
    private String bio;
    private int id;
    private String avatar;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
