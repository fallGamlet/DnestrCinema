package com.fallgamlet.dnestrcinema.network;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.os.ParcelableCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by fallgamlet on 07.01.17.
 */

public class RssItem implements Parcelable {
    //region Fields
    String mTitle;
    String mLink;
    String mDescription;
    String mImgUrl;
    Date mPubDate;
    ArrayList<Schedule> mSchedules = new ArrayList<>();
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.mImgUrl = imgUrl;
    }

    public Date getPubDate() {
        return mPubDate;
    }

    public void setPubDate(Date pubDate) {
        this.mPubDate = pubDate;
    }

    public ArrayList<Schedule> getSchedules() {
        return mSchedules;
    }
    //endregion

    //region Methods
    public RssItem() {

    }

    protected RssItem(Parcel in) {
        setData(in);
    }

    public static final Creator<RssItem> CREATOR = new Creator<RssItem>() {
        @Override
        public RssItem createFromParcel(Parcel in) {
            return new RssItem(in);
        }

        @Override
        public RssItem[] newArray(int size) {
            return new RssItem[size];
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
        dest.writeString(mDescription);
        dest.writeString(mImgUrl);
        dest.writeTypedList(mSchedules);
        dest.writeLong(mPubDate==null? 0: mPubDate.getTime());
    }

    public void setData(Parcel in) {
        mTitle = in.readString();
        mLink = in.readString();
        mDescription = in.readString();
        mImgUrl = in.readString();
        mSchedules = in.createTypedArrayList(Schedule.CREATOR);

        long d = in.readLong();
        mPubDate = d == 0? null: new Date(d);

    }

    public boolean setData(@NonNull JSONObject jObj) throws JSONException {
        mTitle = jObj.optString("title");
        mLink = jObj.optString("link");
        mDescription = jObj.optString("description");

        String hall = jObj.optString("hall");
        String pubDate = jObj.optString("pubDate");
        String seanses = jObj.optString("seanses");

        JSONObject jtmp = jObj.optJSONObject("enclosure");
        if (jtmp != null) {
            jtmp = jtmp.optJSONObject("_attr");
            if (jtmp != null) {
                mImgUrl = jtmp.optString("url");
            }
        }

        if (pubDate != null) {
            try {
                DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
                mPubDate = df.parse(pubDate);
            } catch (Exception e) {
                System.out.println("Date parse fail: "+e.toString());
            }
        }

        //"16.30, 21.30 от 12 лет; борд.: 19.00 от 12 лет; DVD: 15.10 от 12 лет"
        seanses = hall+": "+seanses;
        String[] strArr = seanses.trim().split(";");

        for (int i=0; i<strArr.length; i++) {
            String str = strArr[i];
            String[] arr = str.trim().split(":");
            Schedule schedule = new Schedule();
            if (arr.length >= 1) {
                String roomVal = arr[0];
                if ("борд.".equals(roomVal)) { roomVal = "Бордовый зал"; }
                else if ("DVD".equals(roomVal)) { roomVal = "Малый зал"; }
                schedule.room = roomVal;
                if (arr.length >= 2) {
                    schedule.value = arr[1];
                }
                getSchedules().add(schedule);
            }
        }

        return true;
    }

    @NonNull
    public static ArrayList<RssItem> getItems(JSONArray jArr) throws JSONException {
        ArrayList<RssItem> rssItems = new ArrayList<>();
        if (jArr == null || jArr.length() == 0) {
            return rssItems;
        }

        for (int i=0; i<jArr.length(); i++) {
            JSONObject jObj = jArr.optJSONObject(i);
            if (jObj != null) {
                RssItem item = new RssItem();
                item.setData(jObj);
                rssItems.add(item);
            }
        }

        return rssItems;
    }

    @NonNull
    public static ArrayList<RssItem> parseRSS(String rssText) throws JSONException, ParserConfigurationException, SAXException, IOException {
        Document doc = Xml2JSON.getDocument(rssText);
        JSONObject jsonFull = Xml2JSON.getJSON(doc);
        JSONObject jRes = (JSONObject) Xml2JSON.makeShort(jsonFull);
        Object objItem = jRes.getJSONObject("rss")
                .getJSONObject("channel")
                .get("item");

        JSONArray jItems = null;
        if (objItem instanceof JSONObject) {
            jItems = new JSONArray();
            jItems.put(objItem);
        } else if (objItem instanceof JSONArray) {
            jItems = (JSONArray) objItem;
        }

        return getItems(jItems);
    }

    public static Comparator<RssItem> getDateComparator() {
        return new Comparator<RssItem>() {
            @Override
            public int compare(RssItem item1, RssItem item2) {
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

    public static Comparator<RssItem> getTitleComparator() {
        return new Comparator<RssItem>() {
            @Override
            public int compare(RssItem item1, RssItem item2) {
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

    public static Comparator<RssItem> getDateTitleComparator() {
        return new Comparator<RssItem>() {
            @Override
            public int compare(RssItem item1, RssItem item2) {
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

    //region Sub classes and interfaces
    public static class Schedule implements Parcelable {
        public String room;
        public String value;

        public Schedule() {

        }

        protected Schedule(Parcel in) {
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

        public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
            @Override
            public Schedule createFromParcel(Parcel in) {
                return new Schedule(in);
            }

            @Override
            public Schedule[] newArray(int size) {
                return new Schedule[size];
            }
        };
    }
    //endregion
}
