package bussiness.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private int resultId;
    private int userId;
    private int examId;
    private double totalPoint;
    private LocalDateTime CreatedDate;

    public Result() {
    }

    public Result(int resultId, int userId, int examId, double totalPoint, LocalDateTime createdDate) {
        this.resultId = resultId;
        this.userId = userId;
        this.examId = examId;
        this.totalPoint = totalPoint;
        CreatedDate = createdDate;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public double getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(double totalPoint) {
        this.totalPoint = totalPoint;
    }

    public LocalDateTime getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        CreatedDate = createdDate;
    }
    public void inputData(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result result)) return false;
        return userId == result.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
