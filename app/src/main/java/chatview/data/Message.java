package chatview.data;


import android.net.Uri;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

import viettel.cyberspace.assitant.model.Answer;
import viettel.cyberspace.assitant.model.BaseResponse;
import viettel.cyberspace.assitant.model.ResponseAnswer;

/**
 * Created by shrikanthravi on 16/02/18.
 */

@Table(name = "Message")
public class Message {

    protected boolean isQuestion;

    protected boolean isAnswerFromChuyengia;
    protected long id;
    protected MessageType messageType;
    protected String type;
    protected String body;
    protected String time;
    protected String status;
    protected List<Uri> imageList;
    protected String userName;
    protected Uri userIcon;
    protected Uri videoUri;
    protected Uri audioUri;
    protected int indexPosition;
    protected String mid;
    protected String rateMessage;
    protected boolean isSendMaster;
    protected boolean isAnswer;
    protected String webUrl;
    private BaseResponse baseResponse;
    private ResponseAnswer responseAnswer;
    private long timeStamp;
    private String question;

    public enum MessageType {
        LeftSimpleMessage,
        RightSimpleImage,
        LeftSingleImage,
        RightSingleImage,
        LeftMultipleImages,
        RightMultipleImages,
        LeftVideo,
        RightVideo,
        LeftAudio,
        RightAudio,
        ListQuestion,
        ListSuggestion,
        LeftHtml
    }

    public Message() {

    }

    public void saveMessageHistory() {
        MessageHistory messageHistory = new MessageHistory();
        messageHistory.setBody(body);
        messageHistory.setAnswer(isAnswer);
        messageHistory.setMessageType(messageType.toString());
        messageHistory.setRateMessage(rateMessage);
        messageHistory.setUserName(userName);
        messageHistory.setTime(time);
        messageHistory.setStatus(status);
        messageHistory.setMid(mid);
        messageHistory.setSendMaster(isSendMaster);
        messageHistory.getId();
        messageHistory.setMessageId(id);
        messageHistory.setBaseResponsefromObject(baseResponse);
        messageHistory.setResponseAnswerFromObject(responseAnswer);
        messageHistory.setAnswerFromChuyengia(isAnswerFromChuyengia);
        messageHistory.setTimeStamp(timeStamp);
        messageHistory.setQuestion(question);
        messageHistory.save();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
    }

    public String getRateMessage() {
        return rateMessage;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public void setRateMessage(String rateMessage) {
        this.rateMessage = rateMessage;
    }

    public Uri getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public int getIndexPosition() {
        return indexPosition;
    }

    public void setIndexPosition(int indexPosition) {
        this.indexPosition = indexPosition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<Uri> getImageList() {
        return imageList;
    }

    public void setImageList(List<Uri> imageList) {
        this.imageList = imageList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Uri getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Uri userIcon) {
        this.userIcon = userIcon;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public boolean isSendMaster() {
        return isSendMaster;
    }

    public void setSendMaster(boolean sendMaster) {
        isSendMaster = sendMaster;
    }


    public boolean isQuestion() {
        return isQuestion;
    }

    public void setQuestion(boolean question) {
        isQuestion = question;
    }


    public boolean isAnswerFromChuyengia() {
        return isAnswerFromChuyengia;
    }

    public void setAnswerFromChuyengia(boolean answerFromChuyengia) {
        isAnswerFromChuyengia = answerFromChuyengia;
    }

    public ResponseAnswer getResponseAnswer() {
        return responseAnswer;
    }

    public void setResponseAnswer(ResponseAnswer responseAnswer) {
        this.responseAnswer = responseAnswer;
    }


    @Override
    public String toString() {
        return "Message{" +
                "body='" + body + '\'' +
                ", isAnswer=" + isAnswer +
                '}';
    }
}


