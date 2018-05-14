package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 20/12/2017.
 */

public class User {
    @SerializedName("code")
    private long  code;
    @SerializedName("messange")
    private String messange;

    public String getMessange() {
        return messange;
    }

    public long getCode() {
        return code;
    }
}
