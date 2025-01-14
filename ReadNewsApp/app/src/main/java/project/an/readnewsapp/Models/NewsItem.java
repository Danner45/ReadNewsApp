package project.an.readnewsapp.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsItem {
    private String title, imgUrl,pupDate, link, content;
    private boolean isSave;
    private String category;

    public NewsItem(String title, String imgUrl, String pupDate, String link, String content) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.pupDate = pupDate;
        this.link = link;
        this.content = content;
        this.isSave = false;
    }

    public void formatDay(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ssZ", Locale.ENGLISH);
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
        try{
            Date date = simpleDateFormat.parse(pupDate);
            pupDate = newFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getPupDate() {
        return pupDate;
    }

    public void setPupDate(String pupDate) {
        this.pupDate = pupDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }
}
