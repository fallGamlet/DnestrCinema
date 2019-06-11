package com.fallgamlet.dnestrcinema.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class MovieDetailItem implements Parcelable {

    @SerializedName("description") private String description;
    @SerializedName("genre") private String genre;
    @SerializedName("ageLimit") private String ageLimit;
    @SerializedName("country") private String country;
    @SerializedName("director") private String director;
    @SerializedName("scenario") private String scenario;
    @SerializedName("actors") private String actors;
    @SerializedName("budget") private String budget;
    @SerializedName("imgUrls") private Set<String> imgUrls;

    public MovieDetailItem() {

    }

    protected MovieDetailItem(Parcel in) {
        readFromParcel(in);
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public Set<String> getImgUrls() {
        if (this.imgUrls == null) {
            this.imgUrls = new HashSet<>();
        }

        return imgUrls;
    }

    public void setImgUrls(Set<String> imgUrls) {
        this.imgUrls = imgUrls;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeString(genre);
        parcel.writeString(ageLimit);
        parcel.writeString(country);
        parcel.writeString(director);
        parcel.writeString(scenario);
        parcel.writeString(actors);
        parcel.writeString(budget);
        parcel.writeStringList(new ArrayList<>(getImgUrls()));
    }

    public void readFromParcel(Parcel in) {
        List<String> imgUrlList = new ArrayList<>();

        description = in.readString();
        genre = in.readString();
        ageLimit = in.readString();
        country = in.readString();
        director = in.readString();
        scenario = in.readString();
        actors = in.readString();
        budget = in.readString();
        in.readStringList(imgUrlList);
    }

    public static final Creator<MovieDetailItem> CREATOR = new Creator<MovieDetailItem>() {
        @Override
        public MovieDetailItem createFromParcel(Parcel in) {
            return new MovieDetailItem(in);
        }

        @Override
        public MovieDetailItem[] newArray(int size) {
            return new MovieDetailItem[size];
        }
    };
}
