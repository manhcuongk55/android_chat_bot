package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by utit on 21/12/2017.
 */

public class BaseResponse {
    @SerializedName("status")
    private long status;
    @SerializedName("answerCode")
    private int answerCode;
    @SerializedName("messageList")
    private Answer[] message;

    public Answer[] getMessage() {
        return message;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public int getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode(int answerCode) {
        this.answerCode = answerCode;
    }

    public void setMessage(Answer[] message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", message=" + Arrays.toString(message) +
                '}';
    }
}
