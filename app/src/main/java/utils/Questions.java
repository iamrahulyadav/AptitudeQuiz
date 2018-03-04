package utils;


import com.facebook.ads.NativeAd;

import java.io.Serializable;

/**
 * Created by Aisha on 9/30/2017.
 */

public class Questions implements Serializable {

    private String questionName;
    private String correctAnswer;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;


    transient NativeAd nativeAd;

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    private int randomNumber;
    private String questionExplaination;
    private String questionTopicName;
    private String questionTestName;
    private String previousYearsName;
    private String questionUID;
    private int questionTimer;


    public int getQuestionTimer() {
        return questionTimer;
    }

    public void setQuestionTimer(int questionTimer) {
        this.questionTimer = questionTimer;
    }


    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    private String userAnswer = null;

    public String getQuestionUID() {
        return questionUID;
    }

    public void setQuestionUID(String questionUID) {
        this.questionUID = questionUID;
    }


    public String getQuestionTopicName() {
        return questionTopicName;
    }

    public void setQuestionTopicName(String questionTopicName) {
        this.questionTopicName = questionTopicName;
    }

    public String getQuestionTestName() {
        return questionTestName;
    }

    public void setQuestionTestName(String questionTestName) {
        this.questionTestName = questionTestName;
    }

    public String getPreviousYearsName() {
        return previousYearsName;
    }

    public void setPreviousYearsName(String previousYearsName) {
        this.previousYearsName = previousYearsName;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }


    public String getQuestionExplaination() {
        return questionExplaination;
    }

    public void setQuestionExplaination(String questionExplaination) {
        this.questionExplaination = questionExplaination;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }
}
