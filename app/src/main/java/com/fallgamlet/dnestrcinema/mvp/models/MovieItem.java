package com.fallgamlet.dnestrcinema.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;

import java.util.ArrayList;
import java.util.Comparator;
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
    private String mTitle;
    private String mLink;
    private String mBuyTicketLink;
    private String mDuration;
    private String mDescription;
    private String mGenre;
    private String mAgeLimit;
    private String mCountry;
    private String mDirector;
    private String mScenario;
    private String mActors;
    private String mBudget;

    private String mPosterUrl;
    private Set<String> mImgUrlList;
    private Set<String> mTrailerUrlList;
    private ArrayList<ScheduleItem> mSchedules = new ArrayList<>();
    private Date mPubDate;
    //endregion

    //region Getters and Setters
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public String getBuyTicketLink() {
        return mBuyTicketLink;
    }

    public void setBuyTicketLink(String link) {
        this.mBuyTicketLink = link;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        this.mDuration= duration;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getAgeLimit() {
        return mAgeLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.mAgeLimit = ageLimit;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        this.mGenre= genre;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(String director) {
        this.mDirector = director;
    }

    public String getScenario() {
        return mScenario;
    }

    public void setScenario(String scenario) {
        this.mScenario = scenario;
    }

    public String getActors() {
        return mActors;
    }

    public void setActors(String actors) {
        this.mActors= actors;
    }

    public String getBudget() {
        return mBudget;
    }

    public void setBudget(String budget) {
        this.mBudget = budget;
    }


    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(String imgUrl) {
        this.mPosterUrl = imgUrl;
    }

    public synchronized Set<String> getImgUrlSet() {
        if (mImgUrlList == null) {
            mImgUrlList = new ArraySet<>();
        }
        return mImgUrlList;
    }

    public synchronized Set<String> getTrailerUrlSet() {
        if (mTrailerUrlList == null) {
            mTrailerUrlList = new ArraySet<>();
        }
        return mTrailerUrlList;
    }

    public Date getPubDate() {
        return mPubDate;
    }

    public void setPubDate(Date pubDate) {
        this.mPubDate = pubDate;
    }

    @NonNull
    public ArrayList<ScheduleItem> getSchedules() {
        if (mSchedules == null) {
            mSchedules = new ArrayList<>();
        }
        return mSchedules;
    }
    //endregion

    //region Methods
    public MovieItem() {

    }

    protected MovieItem(Parcel in) {
        setData(in);
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
        dest.writeString(mTitle);
        dest.writeString(mLink);
        dest.writeString(mBuyTicketLink);
        dest.writeString(mDescription);
        dest.writeLong(mPubDate==null? 0: mPubDate.getTime());
        dest.writeString(mPosterUrl);
        dest.writeTypedList(mSchedules);
        dest.writeStringList(new ArrayList<String>(getImgUrlSet()));
        dest.writeStringList(new ArrayList<String>(getTrailerUrlSet()));
    }

    public void setData(Parcel in) {
        List<String> imgUrlList = new ArrayList<>();
        List<String> moveUrlList = new ArrayList<>();

        mTitle = in.readString();
        mLink = in.readString();
        mBuyTicketLink = in.readString();
        mDescription = in.readString();
        long d = in.readLong();
        mPosterUrl = in.readString();
        mSchedules = in.createTypedArrayList(ScheduleItem.CREATOR);
        in.readStringList(imgUrlList);
        in.readStringList(moveUrlList);

        getImgUrlSet().addAll(imgUrlList);
        getTrailerUrlSet().addAll(moveUrlList);

        mPubDate = d == 0? null: new Date(d);
    }

    public void mergeLeft(MovieItem item) {
        if (item == null) {
            return;
        }

        if (getTitle() == null || getTitle().isEmpty()) {
            setTitle(item.getTitle());
        }

        if (getPubDate() == null) {
            setPubDate(item.getPubDate());
        }

        if (getLink() == null || getLink().isEmpty()) {
            setLink(item.getLink());
        }

        if (getBuyTicketLink() == null || getBuyTicketLink().isEmpty()) {
            setBuyTicketLink(item.getBuyTicketLink());
        }

        if (getPosterUrl() == null || getPosterUrl().isEmpty()) {
            setPosterUrl(item.getPosterUrl());
        }

        if (getDirector() == null || getDirector().isEmpty()) {
            setDirector(item.getDirector());
        }

        if (getActors() == null || getActors().isEmpty()) {
            setActors(item.getActors());
        }

        if (getScenario() == null || getScenario().isEmpty()) {
            setScenario(item.getScenario());
        }

        if (getGenre() == null || getGenre().isEmpty()) {
            setGenre(item.getGenre());
        }

        if (getDuration() == null || getDuration().isEmpty()) {
            setDuration(item.getDuration());
        }

        if (getAgeLimit() == null || getAgeLimit().isEmpty()) {
            setAgeLimit(item.getAgeLimit());
        }

        if (getBudget() == null || getBudget().isEmpty()) {
            setBudget(item.getBudget());
        }

        if (getCountry() == null || getCountry().isEmpty()) {
            setCountry(item.getCountry());
        }

        if (getDescription() == null || getDescription().isEmpty()) {
            setDescription(item.getDescription());
        }

        if (getSchedules().isEmpty()) {
            getSchedules().addAll(item.getSchedules());
        }

        if (getImgUrlSet().isEmpty()) {
            getImgUrlSet().addAll(item.getImgUrlSet());
        }

        if (getTrailerUrlSet().isEmpty()) {
            getTrailerUrlSet().addAll(item.getTrailerUrlSet());
        }
    }
    //endregion

    //region Methods for get Comparator
    public static Comparator<MovieItem> getDateComparator() {
        return new Comparator<MovieItem>() {
            @Override
            public int compare(MovieItem item1, MovieItem item2) {
                if (item1 == null) {
                    if (item2 == null) { return 0; }
                    else { return -1; }
                }
                if (item2 == null) { return 1; }

                Date date1 = item1.getPubDate();
                Date date2 = item2.getPubDate();

                if (date1 == null) {
                    if (date2 == null) { return 0; }
                    else { return -1; }
                }
                if (date2 == null) { return 1; }

                return date1.compareTo(date2);
            }
        };
    }

    public static Comparator<MovieItem> getTitleComparator() {
        return new Comparator<MovieItem>() {
            @Override
            public int compare(MovieItem item1, MovieItem item2) {
                if (item1 == null) {
                    if (item2 == null) { return 0; }
                    else { return -1; }
                }
                if (item2 == null) { return 1; }

                String value1 = item1.getTitle();
                String value2 = item2.getTitle();

                if (value1 == null) {
                    if (value2 == null) { return 0; }
                    else { return -1; }
                }
                if (value2 == null) { return 1; }

                return value1.compareTo(value2);
            }
        };
    }

    public static Comparator<MovieItem> getDateTitleComparator() {
        return new Comparator<MovieItem>() {
            @Override
            public int compare(MovieItem item1, MovieItem item2) {
                if (item1 == null) {
                    if (item2 == null) { return 0; }
                    else { return -1; }
                }
                if (item2 == null) { return 1; }

                Date date1 = item1.getPubDate();
                Date date2 = item2.getPubDate();

                if (date1 == null) {
                    if (date2 == null) { return 0; }
                    else { return -1; }
                }
                if (date2 == null) { return 1; }

                int dcomp = date1.compareTo(date2);
                if (dcomp != 0) {
                    return dcomp;
                }

                String title1 = item1.getTitle();
                String title2 = item2.getTitle();

                if (title1 == null) {
                    if (title2 == null) { return 0; }
                    else { return -1; }
                }
                if (title2 == null) { return 1; }

                return title1.compareTo(title2);
            }
        };
    }
    //endregion
}
