package com.kinotir.api.mappers;

import com.kinotir.api.FilmJson;
import com.kinotir.api.FilmSessionJson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

class HtmlFilmsMapper implements Mapper<String, List<FilmJson>> {

    private MovieDateMapper dateMapper = new MovieDateMapper();

    @Override
    public List<FilmJson> map(String html) {
        if (html == null || html.isEmpty()) return null;

        Document doc = Jsoup.parse(html);
        if (doc == null) return null;

        Elements elements = doc.select(".movies-tile>.item>.item-wrapper");
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        List<FilmJson> movies = new ArrayList<>(elements.size());
        for (Element element: elements) {
            FilmJson movie = parseElement(element);
            if (movie != null) {
                movies.add(movie);
            }
        }

        return movies;
    }

    private FilmJson parseElement(Element element) {
        if (element == null) return null;

        String posterUrl = element.select(">.poster>img").attr("src");
        String title = element.select(">h2").text();
        String linkUrl = element.select(">.overlay>a").attr("href");
        String buyTicketUrl = element.select(">.overlay>.by-ticket>a").attr("href");

        FilmJson movieItem = new FilmJson();
        movieItem.setTitle(title);
        movieItem.setPosterUrl(posterUrl);
        movieItem.setLink(linkUrl);
        movieItem.setBuyTicketLink(buyTicketUrl);

        parseFeatures(element.select(">.overlay .features"), movieItem);
        parseSchedule(element.select(">.overlay .halls>li"), movieItem);
        parseTrailers(element.select(">.overlay>.links>a.trailer"), movieItem);

        return movieItem;
    }

    private void parseFeatures(Elements src, FilmJson movieItem) {
        if (src == null) return;

        src = src.select(">li");

        for (Element el: src) {
            String val = el.childNodeSize() >= 2? el.childNode(1).toString().trim(): null;
            if (val != null) {
                if (!el.select(">i:containsOwn(Старт)").isEmpty()) {
                    movieItem.setPubDate(dateMapper.map(val));
                } else if (!el.select("i:containsOwn(Жанр)").isEmpty()) {
                    movieItem.setGenre(val);
                } else if (!el.select("i:containsOwn(Возраст)").isEmpty()) {
                    movieItem.setAgeLimit(val);
                } else if (!el.select("i:containsOwn(Продолжительность)").isEmpty()) {
                    movieItem.setDuration(val);
                }
            }
        }
    }

    private void parseSchedule(Elements src, FilmJson movieItem) {
        List<FilmSessionJson> sessions = new ArrayList<>();
        if (src == null) return;

        for (Element item: src) {
            sessions.add(parseScheduleItem(item));
        }

        movieItem.setSessions(sessions);
    }

    private FilmSessionJson parseScheduleItem(Element item) {
        String roomName = item.select(">h5").text();
        Elements els = item.select(">i");
        Element last = els.last();
        int i= item.childNodes().indexOf(last);

        Node elTime = i>=0 && i+1<item.childNodeSize()? item.childNodes().get(i+1): null;

        String timesStr = elTime==null? null: elTime.toString();

        FilmSessionJson schedule = new FilmSessionJson();
        schedule.setRoom(roomName);
        schedule.setValue(timesStr);

        return schedule;
    }

    private void parseTrailers(Elements src, FilmJson movieItem) {
        List<String> trailerUrls = new ArrayList<>();
        if (src == null) return;

        for (Element el: src) {
            String url = el.attr("href");
            if (url != null && !url.isEmpty()) {
                trailerUrls.add(url);
            }
        }

        movieItem.setTrailerUrls(trailerUrls);
    }
}
