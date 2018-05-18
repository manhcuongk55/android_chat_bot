package viettel.cyberspace.assitant.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by utit on 21/12/2017.
 */
public class BaseResponse {
    @SerializedName("answerCode")
    private long answerCode;

    @SerializedName("messageList")
    private List<Answer> message;

    @SerializedName("status")
    private long status;

    BaseResponse baseResponseSave;

    public List<Answer> getMessage() {
        return message;
    }

    public long getStatus() {
        return status;
    }

    public long getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode(long answerCode) {
        this.answerCode = answerCode;
    }

    public void setMessage(List<Answer> message) {
        this.message = message;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public BaseResponse getBaseResponseSave() {
        return baseResponseSave;
    }

    public void setBaseResponseSave(BaseResponse baseResponseSave) {
        this.baseResponseSave = baseResponseSave;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "answerCode=" + answerCode +
                ", message=" + message +
                ", status=" + status +
                '}';
    }
}
