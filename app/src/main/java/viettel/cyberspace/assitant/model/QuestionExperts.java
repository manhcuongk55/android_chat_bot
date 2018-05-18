package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by brwsr on 17/05/2018.
 */

public class QuestionExperts {
    @SerializedName("question")
    private String question;

    @SerializedName("mid")
    private String mid;

    @SerializedName("createdTime")
    private Long createdTime;

    @SerializedName("userId")
    private int userId;

    @SerializedName("username")
    private String username;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "QuestionExperts{" +
                "question='" + question + '\'' +
                ", mid='" + mid + '\'' +
                ", createdTime=" + createdTime +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
