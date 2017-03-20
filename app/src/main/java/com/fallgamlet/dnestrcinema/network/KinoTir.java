package com.fallgamlet.dnestrcinema.network;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fallgamlet on 19.03.17.
 */

public class KinoTir {
    public static String BASE_URL = "http://kinotir.md";
    public static String PATH_NOW = "/";
    public static String PATH_SOON = "/skoro-v-kino";
    public static String PATH_NEWS = "/novosti";
    public static String PATH_ROOMS = "/zaly-kinoteatra";
    public static String PATH_QA = "/vopros-otvet";
    public static String PATH_ABOUT = "/o-kinoteatre";

    public interface Parser<T> {
        T parse(String body);
    }

    public static class MovieListParser implements Parser<List<MovieItem>> {

        public MovieListParser() {

        }

        public List<MovieItem> parse(String html) {
            if (html == null) {
                return null;
            }
            Document doc = Jsoup.parse(html);
            if (doc == null) {
                return null;
            }

            Elements elements = doc.select(".movies-tile>.item>.item-wrapper");
            if (elements == null || elements.isEmpty()) {
                return null;
            }

            List<MovieItem> movies = new ArrayList<>(elements.size());
            for (Element element: elements) {
                MovieItem movie = parseElement(element);
                if (movie != null) {
                    movies.add(movie);
                }
            }

            return movies;
        }

        private MovieItem parseElement(Element element) {
            if (element == null) {
                return null;
            }

            String posterUrl = element.select(">.poster>img").attr("src");
            String title = element.select(">h2").text();
            String linkUrl = element.select(">.overlay>a").attr("href");

            MovieItem movieItem = new MovieItem();
            parseFeatures(element.select(">.overlay .features"), movieItem);

            movieItem.setTitle(title);
            movieItem.setImgUrl(posterUrl);
            movieItem.setLink(linkUrl);

            parseSchedule(element.select(">.overlay .halls>li"), movieItem.getSchedules());
            parseTrailers(element.select(">.overlay>.links>a.trailer"), movieItem);

            return movieItem;
        }

        private void parseFeatures(Elements src, MovieItem movieItem) {
            if (src == null || movieItem == null) {
                return;
            }

            src = src.select(">li");
            String strStart = null;
            String strGenre = null;
            String strDuration = null;
            String strAgeLimit = null;

            for (Element el: src) {
                String val = el.childNodeSize() >= 2? el.childNode(1).toString().trim(): null;
                if (val != null) {
                    if (!el.select(">i:containsOwn(Старт)").isEmpty()) {
                        strStart = val;
                    } else if (!el.select("i:containsOwn(Жанр)").isEmpty()) {
                        strGenre = val;
                    } else if (!el.select("i:containsOwn(Возраст)").isEmpty()) {
                        strAgeLimit = val;
                    } else if (!el.select("i:containsOwn(Продолжительность)").isEmpty()) {
                        strDuration = val;
                    }
                }
            }

            Date start = parseDate(strStart);

            movieItem.setDuration(strDuration);
            movieItem.setGenre(strGenre);
            movieItem.setPubDate(start);
            movieItem.setAgeLimit(strAgeLimit);
        }

        private void parseSchedule(Elements src, List<MovieItem.Schedule> scheduleList) {
            if (src == null || scheduleList == null) {
                return;
            }
            for (Element item: src) {
                String roomName = item.select(">h5").text();
                Elements els = item.select(">i");
                Element last = els.last();
                int i= item.childNodes().indexOf(last);
                Node elTime = i>=0 && i+1<item.childNodeSize()? item.childNodes().get(i+1): null;
                String timesStr = elTime==null? null: elTime.toString();
                MovieItem.Schedule schedule = new MovieItem.Schedule();
                schedule.room = roomName;
                schedule.value = timesStr;
                scheduleList.add(schedule);
            }
        }

        private Date parseDate(String val) {
            if (val == null) {
                return null;
            }
            Date date = null;
            // с 16 марта, 2017
            String[] arr = val.split(" ");
            SimpleDateFormat format = new SimpleDateFormat("'с' d MMMM, yyyy", Locale.getDefault());
            try {
                date = format.parse(val.trim());
            } catch (ParseException ignored) {
                System.out.println(ignored);
            }
            return date;
        }

        private void parseTrailers(Elements src, MovieItem movieItem) {
            if (src == null || movieItem == null) {
                return;
            }

            for (Element el: src) {
                String url = el.attr("href");
                if (url != null && !url.isEmpty()) {
                    movieItem.getMoveUrlSet().add(url);
                }
            }

        }
    }

    public static class MovieDetailParser implements Parser<MovieItem> {

        @Override
        public MovieItem parse(String html) {
            if (html == null) {return null;}

            Document doc = Jsoup.parse(html);
            if (doc == null) { return null;}

            Element info = doc.select(".info fr").first();
            if (info == null) {return null;}

            String posterUrl = info.select(">.additional-poster>.image>img").attr("src");

            info = info.select(".main-info").first();
            if (info == null) {return null;}




            return null;
        }
    }

}
