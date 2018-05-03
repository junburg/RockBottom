package com.junburg.moon.rockbottom.model;

/**
 * Created by Junburg on 2018. 4. 21..
 */


/**
 * I don't need this model now
 */
public class Learn {

    private String content;

    public Learn() {
    }

    public Learn(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Learn{" +
                "content='" + content + '\'' +
                '}';
    }
}
