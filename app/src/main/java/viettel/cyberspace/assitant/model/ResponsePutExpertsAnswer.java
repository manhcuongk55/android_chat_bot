package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by brwsr on 17/05/2018.
 */

public class ResponsePutExpertsAnswer {
    @SerializedName("status")
    private int status;
    @SerializedName("answerId")
    private String answerId;
    @SerializedName("questionId")
    private String questionId;
    @SerializedName("answer")
    private String answer;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "ResponsePutExpertsAnswer{" +
                "status=" + status +
                ", answerId='" + answerId + '\'' +
                ", questionId='" + questionId + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
