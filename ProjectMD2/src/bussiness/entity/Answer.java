package bussiness.entity;

import java.io.Serializable;

public class Answer implements Serializable {

    private int answerId;
    private String answerContent;
    private boolean answerTrue;


    public Answer() {
    }

    public Answer(int answerId, String answerContent, boolean answerTrue) {
        this.answerId = answerId;
        this.answerContent = answerContent;
        this.answerTrue = answerTrue;

    }

    public int getAnswerId() {
        return answerId;
    }


    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public boolean getAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }

}
