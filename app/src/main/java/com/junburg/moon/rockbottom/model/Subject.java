package com.junburg.moon.rockbottom.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Junburg on 2018. 4. 19..
 */

public class Subject {

    private String name;
    private String explain;
    private String subject_id;
    private List<Chapter> chapterList;

    public Subject() {
    }

    public Subject(String name, String explain, String subject_id, List<Chapter> chapterList) {
        this.name = name;
        this.explain = explain;
        this.subject_id = subject_id;
        this.chapterList = chapterList;
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

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public List<Chapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", explain='" + explain + '\'' +
                ", subject_id='" + subject_id + '\'' +
                ", chapterList=" + chapterList +
                '}';
    }
}
