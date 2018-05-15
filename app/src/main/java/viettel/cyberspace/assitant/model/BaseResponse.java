package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

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
}
