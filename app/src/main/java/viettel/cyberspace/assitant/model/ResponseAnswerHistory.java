package viettel.cyberspace.assitant.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 21/12/2017.
 */
@Table(name = "ResponseAnswerHistory")
public class ResponseAnswerHistory extends Model {
    @Column(name = "answerId")
    private String answerId;
    @Column(name = "questionId")
    private String questionId;
    @Column(name = "createdTime")
    private long createdTime;
    @Column(name = "expertUserId")
    private int expertUserId;
    @Column(name = "expertUsername")
    private String expertUsername;
    @Column(name = "question")
    private String question;
    @Column(name = "answer")
    private String answer;


    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getExpertUserId() {
        return expertUserId;
    }

    public void setExpertUserId(int expertUserId) {
        this.expertUserId = expertUserId;
    }

    public String getExpertUsername() {
        return expertUsername;
    }

    public void setExpertUsername(String expertUserName) {
        this.expertUsername = expertUserName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "ResponseAnswerHistory{" +
                "answerId='" + answerId + '\'' +
                ", questionId='" + questionId + '\'' +
                ", createdTime=" + createdTime +
                ", expertUserId=" + expertUserId +
                ", expertUserName='" + expertUsername + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
