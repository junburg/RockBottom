package com.junburg.moon.rockbottom.model;

/**
 * Created by Junburg on 2018. 4. 19..
 */

public class Chapter {

    private String name;
    private String explain;
    private String chapter_id;

    public Chapter() {
    }

    public Chapter(String name, String explain, String chapter_id) {
        this.name = name;
        this.explain = explain;
        this.chapter_id = chapter_id;
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

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "name='" + name + '\'' +
                ", explain='" + explain + '\'' +
                ", chapter_id='" + chapter_id + '\'' +
                '}';
    }
}
