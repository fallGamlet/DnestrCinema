package com.fallgamlet.dnestrcinema.network;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
import android.text.Html;
import android.text.Spanned;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

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
    private String mDuration;
    private String mDescription;
    private String mGenre;
    private String mAgeLimit;
    private String mImgUrl;
    private Set<String> mImgUrlList;
    private Set<String> mMoveUrlList;
    private ArrayList<Schedule> mSchedules = new ArrayList<>();
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

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.mImgUrl = imgUrl;
    }

    public Set<String> getImgUrlSet() {
        if (mImgUrlList == null) {
            mImgUrlList = new ArraySet<>();
        }
        return mImgUrlList;
    }

    public Set<String> getMoveUrlSet() {
        if (mMoveUrlList == null) {
            mMoveUrlList = new ArraySet<>();
        }
        return mMoveUrlList;
    }

    public Date getPubDate() {
        return mPubDate;
    }

    public void setPubDate(Date pubDate) {
        this.mPubDate = pubDate;
    }

    @NonNull
    public ArrayList<Schedule> getSchedules() {
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
        dest.writeString(mDescription);
        dest.writeLong(mPubDate==null? 0: mPubDate.getTime());
        dest.writeString(mImgUrl);
        dest.writeTypedList(mSchedules);
        dest.writeStringList(new ArrayList<String>(getImgUrlSet()));
        dest.writeStringList(new ArrayList<String>(getMoveUrlSet()));
    }

    public void setData(Parcel in) {
        List<String> imgUrlList = new ArrayList<>();
        List<String> moveUrlList = new ArrayList<>();

        mTitle = in.readString();
        mLink = in.readString();
        mDescription = in.readString();
        long d = in.readLong();
        mImgUrl = in.readString();
        mSchedules = in.createTypedArrayList(Schedule.CREATOR);
        in.readStringList(imgUrlList);
        in.readStringList(moveUrlList);

        getImgUrlSet().addAll(imgUrlList);
        getMoveUrlSet().addAll(moveUrlList);

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
                if ("борд.".equals(roomVal)) { roomVal = ROOM_BORDO; }
                else if ("DVD".equals(roomVal)) { roomVal = ROOM_DVD; }
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
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.putOpt("title", this.getTitle());
        json.putOpt("desc", this.getDescription());
        json.putOpt("img_url", this.getImgUrl());
        json.putOpt("img_url_list", new JSONArray(getImgUrlSet()));
        json.putOpt("move_url_list", new JSONArray(getMoveUrlSet()));
        json.putOpt("link", this.getLink());
        json.putOpt("pub_date", this.getPubDate()==null? 0: this.getPubDate().getTime());
        json.putOpt("schedule", Schedule.toJSONArray(this.getSchedules()));
        return json;
    }

    public boolean setJSON(@Nullable JSONObject json) {
        if (json == null || json.length() == 0) { return false; }
        setTitle(json.optString("title", null));
        setDescription(json.optString("desc", null));
        setImgUrl(json.optString("img_url", null));
        setLink(json.optString("link", null));
        JSONArray jarrImgUrls = json.optJSONArray("img_url_list");
        JSONArray jarrMoveUrls = json.optJSONArray("move_url_list");
        long time = json.optLong("pub_date", 0);
        setPubDate(time==0? null: new Date(time));

        if (jarrImgUrls != null && jarrImgUrls.length() > 0) {
            Set<String> urls = getImgUrlSet();
            urls.clear();
            for (int i=0; i<jarrImgUrls.length(); i++) {
                String val = jarrImgUrls.optString(i, null);
                if (val != null && !val.isEmpty()) { urls.add(val); }
            }
        }

        if (jarrMoveUrls != null && jarrMoveUrls.length() > 0) {
            Set<String> urls = getMoveUrlSet();
            urls.clear();
            for (int i=0; i<jarrMoveUrls.length(); i++) {
                String val = jarrMoveUrls.optString(i, null);
                if (val != null && !val.isEmpty()) { urls.add(val); }
            }
        }

        ArrayList<Schedule> schedules = Schedule.setJSONArray(json.optJSONArray("schedule"));
        this.getSchedules().clear();
        this.getSchedules().addAll(schedules);

        return true;
    }

    @NonNull
    public static JSONArray toJSONArray(@Nullable List<MovieItem> items) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        if (items != null && !items.isEmpty()) {
            for (MovieItem item: items) {
                jsonArray.put(item.toJSON());
            }
        }
        return jsonArray;
    }

    @NonNull
    public static ArrayList<MovieItem> setJSONArray(@Nullable JSONArray jarr) {
        ArrayList<MovieItem> list = new ArrayList<>();
        if (jarr != null && jarr.length() > 0) {
            MovieItem item=null;
            for (int i=0; i<jarr.length(); i++) {
                JSONObject json = jarr.optJSONObject(i);
                if (item == null) { item = new MovieItem(); }
                if (item.setJSON(json)) {
                    list.add(item);
                    item = null;
                }
            }
        }
        return list;
    }

    @NonNull
    public static ArrayList<MovieItem> getItems(JSONArray jArr) throws JSONException {
        ArrayList<MovieItem> movies = new ArrayList<>();
        if (jArr == null || jArr.length() == 0) {
            return movies;
        }

        for (int i=0; i<jArr.length(); i++) {
            JSONObject jObj = jArr.optJSONObject(i);
            if (jObj != null) {
                MovieItem item = new MovieItem();
                item.setData(jObj);
                movies.add(item);
            }
        }

        return movies;
    }

    @NonNull
    public static ArrayList<MovieItem> parseRSS(String rssText) throws JSONException, ParserConfigurationException, SAXException, IOException {
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

    public static Spanned fromHtml(String html) {
        if (html == null) {
            return null;
        }
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
    //endregion

    //region Sub classes and interfaces
    public static class Schedule implements Parcelable {
        public String room;
        public String value;

        public Schedule() {

        }

        protected Schedule(Parcel in) {
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

        @NonNull
        public JSONObject toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.putOpt("room", room);
            json.putOpt("value", value);
            return json;
        }

        public boolean setJSON(@Nullable JSONObject json) {
            if (json == null) { return false; }
            room = json.optString("room");
            value = json.optString("value");
            return true;
        }

        @NonNull
        public static JSONArray toJSONArray(@Nullable List<Schedule> items) throws JSONException {
            JSONArray jsonArray = new JSONArray();
            if (items != null && !items.isEmpty()) {
                for (Schedule item: items) {
                    jsonArray.put(item.toJSON());
                }
            }
            return jsonArray;
        }

        @NonNull
        public static ArrayList<Schedule> setJSONArray(@Nullable JSONArray jarr) {
            ArrayList<Schedule> list = new ArrayList<>();
            if (jarr != null && jarr.length() > 0) {
                Schedule item=null;
                for (int i=0; i<jarr.length(); i++) {
                    JSONObject json = jarr.optJSONObject(i);
                    if (item == null) { item = new Schedule(); }
                    if (item.setJSON(json)) {
                        list.add(item);
                        item = null;
                    }
                }
            }
            return list;
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
