package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 20/12/2017.
 */

public class User {
    @SerializedName("status")
    private int status;
    @SerializedName("userId")
    private int userId;
    @SerializedName("token")
    private String token;
    @SerializedName("description")
    private String description;
    @SerializedName("user_type")
    private String user_type;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "User{" +
                "status=" + status +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", description='" + description + '\'' +
                ", user_type='" + user_type + '\'' +
                '}';
    }
}
