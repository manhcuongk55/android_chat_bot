package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 21/12/2017.
 */

public class ResponseMessage {
    @SerializedName("status")
    private long status;
    @SerializedName("description")
    private String description;
    @SerializedName("mid")
    private String mid;


    public String getDescription() {
        return description;
    }

    public long getStatus() {
        return status;
    }

    public String getMid() {
        return mid;
    }
}
