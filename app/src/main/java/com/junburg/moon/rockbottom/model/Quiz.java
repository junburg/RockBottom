package com.junburg.moon.rockbottom.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Junburg on 2018. 5. 24..
 */

public class Quiz implements Parcelable {

    private String question;
    private String firstExample;
    private String secondExample;
    private String thirdExample;
    private String fourthExample;
    private int answerNumber;

    public Quiz() {

    }

    protected Quiz(Parcel in) {
        question = in.readString();
        firstExample = in.readString();
        secondExample = in.readString();
        thirdExample = in.readString();
        fourthExample = in.readString();
        answerNumber = in.readInt();
    }


    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFirstExample() {
        return firstExample;
    }

    public void setFirstExample(String firstExample) {
        this.firstExample = firstExample;
    }

    public String getSecondExample() {
        return secondExample;
    }

    public void setSecondExample(String secondExample) {
        this.secondExample = secondExample;
    }

    public String getThirdExample() {
        return thirdExample;
    }

    public void setThirdExample(String thirdExample) {
        this.thirdExample = thirdExample;
    }

    public String getFourthExample() {
        return fourthExample;
    }

    public void setFourthExample(String fourthExample) {
        this.fourthExample = fourthExample;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public static Creator<Quiz> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "question='" + question + '\'' +
                ", firstExample='" + firstExample + '\'' +
                ", secondExample='" + secondExample + '\'' +
                ", thirdExample='" + thirdExample + '\'' +
                ", fourthExample='" + fourthExample + '\'' +
                ", answerNumber=" + answerNumber +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(firstExample);
        dest.writeString(secondExample);
        dest.writeString(thirdExample);
        dest.writeString(fourthExample);
        dest.writeInt(answerNumber);

    }
}
