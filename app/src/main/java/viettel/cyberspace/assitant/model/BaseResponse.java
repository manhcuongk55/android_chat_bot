package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by utit on 21/12/2017.
 */

public class BaseResponse {


    @SerializedName("answerCode")
    private long answerCode;

    @SerializedName("messageList")
    private Answer[] message;

    @SerializedName("status")
    private long status;

    public Answer[] getMessage() {
        return message;
    }

    public long getStatus() {
        return status;
    }

    public long getAnswerCode() {
        return answerCode;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "answerCode=" + answerCode +
                ", message=" + Arrays.toString(message) +
                ", status=" + status +
                '}';
    }
}
