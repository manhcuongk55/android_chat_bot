package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 21/12/2017.
 */

public class Response {
    @SerializedName("status")
    private long status;
    @SerializedName("description")
    private String description;

    public String getDescription() {
        return description;
    }

    public long getStatus() {
        return status;
    }
}
