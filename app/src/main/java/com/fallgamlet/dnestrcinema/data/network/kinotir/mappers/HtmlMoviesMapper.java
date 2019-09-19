package com.fallgamlet.dnestrcinema.data.network.kinotir.mappers;

import com.fallgamlet.dnestrcinema.data.network.Mapper;
import com.fallgamlet.dnestrcinema.domain.models.MovieDetailItem;
import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.domain.models.ScheduleItem;
import com.fallgamlet.dnestrcinema.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class HtmlMoviesMapper implements Mapper<String, List<MovieItem>> {

    private MovieDateMapper dateMapper = new MovieDateMapper();

    @Override
    public List<MovieItem> map(String html) {
        if (StringUtils.INSTANCE.isEmpty(html)) {
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
        String buyTicketUrl = element.select(">.overlay>.by-ticket>a").attr("href");

        MovieItem movieItem = new MovieItem();
        parseFeatures(element.select(">.overlay .features"), movieItem);

        movieItem.setTitle(title);
        movieItem.setPosterUrl(posterUrl);
        movieItem.setLink(linkUrl);
        movieItem.setBuyTicketLink(buyTicketUrl);

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

        Date start = dateMapper.map(strStart);
        MovieDetailItem detail = movieItem.getDetails();

        movieItem.setDuration(strDuration);
        movieItem.setPubDate(start);
        detail.setGenre(strGenre);
        detail.setAgeLimit(strAgeLimit);
    }

    private void parseSchedule(Elements src, List<ScheduleItem> scheduleList) {
        if (src == null || scheduleList == null) {
            return;
        }

        for (Element item: src) {
            ScheduleItem schedule = parseScheduleItem(item);
            scheduleList.add(schedule);
        }
    }

    private ScheduleItem parseScheduleItem(Element item) {
        String roomName = item.select(">h5").text();
        Elements els = item.select(">i");
        Element last = els.last();
        int i= item.childNodes().indexOf(last);

        Node elTime = i>=0 && i+1<item.childNodeSize()? item.childNodes().get(i+1): null;

        String timesStr = elTime==null? null: elTime.toString();

        ScheduleItem schedule = new ScheduleItem();
        schedule.room = roomName;
        schedule.value = timesStr;

        return schedule;
    }

    private void parseTrailers(Elements src, MovieItem movieItem) {
        if (src == null || movieItem == null) {
            return;
        }

        for (Element el: src) {
            String url = el.attr("href");
            if (url != null && !url.isEmpty()) {
                movieItem.getTrailerUrlSet().add(url);
            }
        }

    }
}
