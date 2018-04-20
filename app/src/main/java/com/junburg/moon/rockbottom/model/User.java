package com.junburg.moon.rockbottom.model;

/**
 * Created by Junburg on 2018. 3. 20..
 */

public class User {

    private String selfieUri;
    private String nickName;
    private String message;
    private String teamName;
    private String github;
    private int points;
    private int ranking;

    public User() {

    }

    public String getSelfieUri() {
        return selfieUri;
    }

    public void setSelfieUri(String selfieUri) {
        this.selfieUri = selfieUri;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}
