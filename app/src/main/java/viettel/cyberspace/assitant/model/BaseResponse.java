package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 21/12/2017.
 */

public class BaseResponse {
    @SerializedName("status")
    private long status;
    @SerializedName("messageList")
    private Answer[] message;

    public Answer[] getMessage() {
        return message;
    }

    public long getStatus() {
        return status;
    }

}
