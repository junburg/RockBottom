package com.junburg.moon.rockbottom.model;

/**
 * Created by Junburg on 2018. 4. 19..
 */

public class Chapter {

    private String name;
    private String explain;

    public Chapter() {
    }

    public Chapter(String name, String explain) {
        this.name = name;
        this.explain = explain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "name='" + name + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }
}
