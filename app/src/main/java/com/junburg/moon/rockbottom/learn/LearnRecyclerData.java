package com.junburg.moon.rockbottom.learn;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class LearnRecyclerData {

    private String learnSubject;
    private String learnContent;

    public LearnRecyclerData(String learnSubject, String learnContent) {
        this.learnSubject = learnSubject;
        this.learnContent = learnContent;
    }

    public String getLearnSubject() {
        return learnSubject;
    }

    public void setLearnSubject(String learnSubject) {
        this.learnSubject = learnSubject;
    }

    public String getLearnContent() {
        return learnContent;
    }

    public void setLearnContent(String learnContent) {
        this.learnContent = learnContent;
    }
}
