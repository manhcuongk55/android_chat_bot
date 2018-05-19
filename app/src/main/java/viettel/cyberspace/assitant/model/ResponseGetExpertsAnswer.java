package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by brwsr on 18/05/2018.
 */

public class ResponseGetExpertsAnswer {
    @SerializedName("status")
    private int status;
    @SerializedName("answerList")
    private List<ResponseAnswer> answerList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResponseAnswer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<ResponseAnswer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public String toString() {
        return "ResponseGetExpertsAnswer{" +
                "status=" + status +
                ", answerList=" + answerList +
                '}';
    }
}
