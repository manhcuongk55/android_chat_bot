package chatview.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.Gson;

import viettel.cyberspace.assitant.model.BaseResponse;
import viettel.cyberspace.assitant.model.ResponseAnswer;
import viettel.cyberspace.assitant.model.User;

/**
 * Created by brwsr on 16/05/2018.
 */
@Table(name = "MessageHistory")
public class MessageHistory extends Model {

    @Column(name = "messageId")
    private Long messageId;
    @Column(name = "messageType")
    private String messageType;
    @Column(name = "body")
    private String body;
    @Column(name = "time")
    private String time;
    @Column(name = "status")
    private String status;
    @Column(name = "userName")
    private String userName;
    @Column(name = "mid")
    private String mid;
    @Column(name = "rateMessage")
    private String rateMessage;
    @Column(name = "isSendMaster")
    private boolean isSendMaster;
    @Column(name = "isAnswer")
    private boolean isAnswer;
    @Column(name = "baseResponse")
    private String baseResponse;
    @Column(name = "isAnswerFromChuyengia")
    private boolean isAnswerFromChuyengia;
    @Column(name = "responseAnswer")
    private String responseAnswer;
    @Column(name = "question")
    private String question;

    @Column(name = "timeStamp", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long timeStamp;

    public MessageHistory() {
        super();
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getRateMessage() {
        return rateMessage;
    }

    public void setRateMessage(String rateMessage) {
        this.rateMessage = rateMessage;
    }

    public boolean isSendMaster() {
        return isSendMaster;
    }

    public void setSendMaster(boolean sendMaster) {
        isSendMaster = sendMaster;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public String getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(String baseResponse) {
        this.baseResponse = baseResponse;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setBaseResponsefromObject(BaseResponse baseResponse) {
        Gson gson = new Gson();
        String json;
        if (baseResponse != null)
            json = gson.toJson(baseResponse);
        else {
            json = "";
        }
        this.baseResponse = json;
    }

    public void setResponseAnswerFromObject(ResponseAnswer responseAnswer) {
        Gson gson = new Gson();
        String json;
        if (responseAnswer != null)
            json = gson.toJson(responseAnswer);
        else {
            json = "";
        }
        this.responseAnswer = json;
    }


    public BaseResponse getBaseResponseFromObject() {
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(this.baseResponse, BaseResponse.class);
        return baseResponse;
    }

    public ResponseAnswer getResponseAnswerFromObject() {
        Gson gson = new Gson();
        ResponseAnswer responseAnswer = gson.fromJson(this.responseAnswer, ResponseAnswer.class);
        return responseAnswer;
    }

    public boolean isAnswerFromChuyengia() {
        return isAnswerFromChuyengia;
    }

    public void setAnswerFromChuyengia(boolean answerFromChuyengia) {
        isAnswerFromChuyengia = answerFromChuyengia;
    }

    public String getResponseAnswer() {
        return responseAnswer;
    }

    public void setResponseAnswer(String responseAnswer) {
        this.responseAnswer = responseAnswer;
    }

    public Message toMessage() {
        Message message = new Message();
        message.setBody(body);
        message.setAnswer(isAnswer);
        message.setMessageType(Message.MessageType.valueOf(messageType.toString()));
        message.setRateMessage(rateMessage);
        message.setUserName(userName);
        message.setTime(time);
        message.setStatus(status);
        message.setMid(mid);
        message.setSendMaster(isSendMaster);
        message.setId(getId());
        message.setId(messageId);
        message.setBaseResponse(getBaseResponseFromObject());
        message.setResponseAnswer(getResponseAnswerFromObject());
        message.setAnswerFromChuyengia(isAnswerFromChuyengia);
        message.setTimeStamp(timeStamp);
        message.setQuestion(question);
        return message;
    }

    @Override
    public String toString() {
        if (getBaseResponseFromObject() == null) {
            return "MessageHistory{" +
                    "messageId=" + messageId +
                    ", messageType='" + messageType + '\'' +
                    ", body='" + body + '\'' +
                    ", time='" + time + '\'' +
                    ", status='" + status + '\'' +
                    ", userName='" + userName + '\'' +
                    ", mid='" + mid + '\'' +
                    ", rateMessage='" + rateMessage + '\'' +
                    ", isSendMaster=" + isSendMaster +
                    ", isAnswer=" + isAnswer +
                    ", baseResponse=" +
                    '}';
        } else {
            return "MessageHistory{" +
                    "messageId=" + messageId +
                    ", messageType='" + messageType + '\'' +
                    ", body='" + body + '\'' +
                    ", time='" + time + '\'' +
                    ", status='" + status + '\'' +
                    ", userName='" + userName + '\'' +
                    ", mid='" + mid + '\'' +
                    ", rateMessage='" + rateMessage + '\'' +
                    ", isSendMaster=" + isSendMaster +
                    ", isAnswer=" + isAnswer +
                    ", baseResponse=" + getBaseResponseFromObject().toString() +
                    '}';
        }
    }
}
