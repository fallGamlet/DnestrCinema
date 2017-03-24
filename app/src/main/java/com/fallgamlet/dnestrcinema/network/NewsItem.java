package com.fallgamlet.dnestrcinema.network;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by fallgamlet on 24.03.17.
 */

public class NewsItem implements Parcelable {
    //region Fields
    private long id;
    private String tag;
    private String title;
    private Date date;
    private String body;
    private Set<String> imgUrls;
    //endregion

    //region Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @NonNull
    public Set<String> getImgUrls() {
        if (imgUrls == null) {
            imgUrls = new TreeSet<>();
        }
        return imgUrls;
    }
    //endregion

    //region Constructors
    public NewsItem() {

    }

    protected NewsItem(Parcel in) {
        setData(in);
    }
    //endregion

    //region Parcelable implementation
    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(body);
    }

    public void setData(Parcel in) {
        id = in.readLong();
        title = in.readString();
        body = in.readString();
    }
    //endregion

    //region genID singleton
    public static long __id=0;
    public static long genID() {
        return ++__id;
    }
    //endregion

    //region Methods


    //endregion
}
