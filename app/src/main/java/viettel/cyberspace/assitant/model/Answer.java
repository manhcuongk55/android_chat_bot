package viettel.cyberspace.assitant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by utit on 21/12/2017.
 */

public class Answer {
    @SerializedName("images")
    public String images;
    @SerializedName("text")
    public String text;
    @SerializedName("html")
    public String html;
    @SerializedName("mid")
    public String mid;
    @SerializedName("title")
    public String title;

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

    @Override
    public String toString() {
        return "Answer{" +
                "images='" + images + '\'' +
                ", text='" + text + '\'' +
                ", html='" + html + '\'' +
                ", mid='" + mid + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
