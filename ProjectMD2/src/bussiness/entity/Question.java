package bussiness.entity;

import bussiness.config.InputMethods;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    private int questionId;
    private String questionContent;
    private List<Answer> answerOption;
    private int answerTrue;

    public Question() {
    }

    public Question(int questionId, String questionContent, List<Answer> answerOption, int answerTrue) {
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.answerOption = answerOption;
        this.answerTrue = answerTrue;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<Answer> getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(List<Answer> answerOption) {
        this.answerOption = answerOption;
    }

    public int getAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(int answerTrue) {
        this.answerTrue = answerTrue;
    }



}
