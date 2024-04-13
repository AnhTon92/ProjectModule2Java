package bussiness.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static bussiness.config.Color.*;
import static bussiness.config.Color.YELLOW_BOLD_BRIGHT;
import static bussiness.entity.RoleName.ADMIN;

public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;
    private int examId;
    private int userId;
    private String description;
    private long duration;
    private List<Question> listQuestion;
    private Date CreatedAt;
    private boolean status = true;
    private List<Catalog> categories;

    public Exam() {
    }

    public Exam(int examId, int userId, String description, long duration, List<Question> listQuestion, Date createdAt, boolean status, List<Catalog> categories) {
        this.examId = examId;
        this.userId = userId;
        this.description = description;
        this.duration = duration;
        this.listQuestion = listQuestion;
        CreatedAt = createdAt;
        this.status = status;
        this.categories = categories;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<Question> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(List<Question> listQuestion) {
        this.listQuestion = listQuestion;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Catalog> getCategories() {
        return categories;
    }

    public void setCategories(List<Catalog> categories) {
        this.categories = categories;
    }
    public void displayData(List<Users> usersList) {
        System.out.println("Mã bài thi:" + this.examId);
        System.out.println("Người tạo đề thi: " + Objects.requireNonNull(usersList.stream()
                .filter(users -> users.getUserId() == this.userId)
                .findFirst().orElse(null)).getUserName());
        System.out.println("Mô tả bài thi: " + this.description);
        System.out.println("Thời gian của bài thi: " + this.duration);
        System.out.println("Danh sách câu hỏi của bài thi: "+ this.listQuestion);
        System.out.println("Ngày tạo bài thi: " + this.getCreatedAt());
        System.out.println("Trạng thái bài thi: "+this.isStatus());
        System.out.println("Danh mục của bài thi: "+ this.categories);
    }

    public void display() {
        System.out.printf(YELLOW_BOLD_BRIGHT + "|" + WHITE_BOLD_BRIGHT + " %-4d " +
                YELLOW_BOLD_BRIGHT + "|" + WHITE_BOLD_BRIGHT + " %-22s " +
                YELLOW_BOLD_BRIGHT + "|" + WHITE_BOLD_BRIGHT + " %-24s " +
                "|" + (status ? (GREEN_BOLD_BRIGHT + " %-13s "
                + YELLOW_BOLD_BRIGHT) : (RED_BOLD_BRIGHT + " %-13s " + YELLOW_BOLD_BRIGHT))
                + "|\n", examId, description, duration, (status ? "Hoạt động" : "Ngừng hoạt động"));
    }
}

