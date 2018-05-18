package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by brwsr on 17/05/2018.
 */

public class ResponseQuestionMaster {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("mid")
    private String mid;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}
