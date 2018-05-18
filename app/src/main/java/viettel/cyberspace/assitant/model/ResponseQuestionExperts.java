package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by brwsr on 17/05/2018.
 */

public class ResponseQuestionExperts {
    @SerializedName("status")
    private int status;

    @SerializedName("questionList")
    private List<QuestionExperts> questionList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<QuestionExperts> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionExperts> questionList) {
        this.questionList = questionList;
    }

    @Override
    public String toString() {
        return "ResponseQuestionExperts{" +
                "status=" + status +
                ", questionList=" + questionList +
                '}';
    }
}
