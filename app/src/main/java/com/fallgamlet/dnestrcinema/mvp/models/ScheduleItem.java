package com.fallgamlet.dnestrcinema.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fallgamlet on 16.07.17.
 */

public class ScheduleItem implements Parcelable {
    public String room;
    public String value;

    public ScheduleItem() {

    }

    protected ScheduleItem(Parcel in) {
        setData(in);
    }

    void setData(Parcel in) {
        room = in.readString();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(room);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ScheduleItem> CREATOR = new Creator<ScheduleItem>() {
        @Override
        public ScheduleItem createFromParcel(Parcel in) {
            return new ScheduleItem(in);
        }

        @Override
        public ScheduleItem[] newArray(int size) {
            return new ScheduleItem[size];
        }
    };
}
