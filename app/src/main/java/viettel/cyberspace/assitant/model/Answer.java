package viettel.cyberspace.assitant.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
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

    @SerializedName("intent_type")
    public String type;

    public String domain;

    public boolean isfocus;

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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isIsfocus() {
        return isfocus;
    }

    public void setIsfocus(boolean isfocus) {
        this.isfocus = isfocus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                ", type='" + type + '\'' +
                ", domain='" + domain + '\'' +
                ", isfocus=" + isfocus +
                '}';
    }
}
