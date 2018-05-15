package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 21/12/2017.
 */

public class Answer {

    @SerializedName("voice")
    public String voice;

    @SerializedName("images")
    public String images;

    @SerializedName("mid")
    public String mid;

    @SerializedName("html")
    public String html;

    @SerializedName("text")
    public String text;

    @SerializedName("title")
    public String title;

    @SerializedName("url")
    public String url;

    public String getImages() {
        return images;
    }

    public String getText() {
        return text;
    }

    public String getHtml() {
        return html;
    }

    public String getMid() {
        return mid;
    }

    public String getTitle() {
        return title;
    }

    public String getVoice() {
        return voice;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "voice='" + voice + '\'' +
                ", images='" + images + '\'' +
                ", mid='" + mid + '\'' +
                ", html='" + html + '\'' +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
