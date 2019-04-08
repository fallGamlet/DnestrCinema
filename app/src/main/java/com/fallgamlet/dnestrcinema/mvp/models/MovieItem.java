package com.fallgamlet.dnestrcinema.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by fallgamlet on 07.01.17.
 */

public class MovieItem implements Parcelable {

    //region Static Constants
    public static final String ROOM_BLUE = "Синий зал";
    public static final String ROOM_BORDO = "Бордовый зал";
    public static final String ROOM_DVD = "Малый зал";
    //endregion

    //region Fields
    @SerializedName("title") private String title;
    @SerializedName("link") private String link;
    @SerializedName("buyLink") private String buyTicketLink;
    @SerializedName("duration") private String duration;
    @SerializedName("posterUrl") private String posterUrl;
    @SerializedName("trailerUrls") private Set<String> mTrailerUrlList;
    @SerializedName("schedulers") private ArrayList<ScheduleItem> mSchedules = new ArrayList<>();
    @SerializedName("pubDate") private Date pubDate;
    @SerializedName("detail") private MovieDetailItem detail;
    //endregion

    //region Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBuyTicketLink() {
        return buyTicketLink;
    }

    public void setBuyTicketLink(String link) {
        this.buyTicketLink = link;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String imgUrl) {
        this.posterUrl = imgUrl;
    }

    public synchronized Set<String> getTrailerUrlSet() {
        if (mTrailerUrlList == null) {
            mTrailerUrlList = new ArraySet<>();
        }
        return mTrailerUrlList;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @NonNull
    public ArrayList<ScheduleItem> getSchedules() {
        if (mSchedules == null) {
            mSchedules = new ArrayList<>();
        }
        return mSchedules;
    }

    @NonNull
    public synchronized MovieDetailItem getDetail() {
        if (detail == null) {
            detail = new MovieDetailItem();
        }

        return detail;
    }

    public void setDetail(MovieDetailItem detail) {
        this.detail = detail;
    }
    //endregion

    //region Methods
    public MovieItem() {

    }

    protected MovieItem(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(buyTicketLink);
        dest.writeLong(pubDate ==null? 0: pubDate.getTime());
        dest.writeString(posterUrl);
        dest.writeTypedList(mSchedules);
        dest.writeStringList(new ArrayList<>(getTrailerUrlSet()));
        dest.writeParcelable(detail, flags);
    }

    public void readFromParcel(Parcel in) {
        List<String> imgUrlList = new ArrayList<>();
        List<String> moveUrlList = new ArrayList<>();

        title = in.readString();
        link = in.readString();
        buyTicketLink = in.readString();
        long d = in.readLong();
        posterUrl = in.readString();
        mSchedules = in.createTypedArrayList(ScheduleItem.CREATOR);
        in.readStringList(moveUrlList);
        detail = in.readParcelable(MovieDetailItem.class.getClassLoader());

        getTrailerUrlSet().addAll(moveUrlList);
        pubDate = d == 0? null: new Date(d);
    }
    //endregion
}
