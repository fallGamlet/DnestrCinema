package com.kinotir.api.mappers;

import com.kinotir.api.FilmDetailsJson;
import com.kinotir.api.ImageUrlJson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


class HtmlFilmDetailMapper implements Mapper<String, FilmDetailsJson> {

    private MovieDateMapper dateMapper = new MovieDateMapper();

    @Override
    public FilmDetailsJson map(String html) {
        if (html == null) {
            return null;
        }

        Document doc = Jsoup.parse(html);
        if (doc == null) {
            return null;
        }

        String buyTicketUrl = doc.select("a.buy-ticket-btn").attr("href");

        Element info = doc.select(".film .info").first();
        if (info == null) {
            return null;
        }

        String posterUrl = info.select(">.additional-poster>.image>img").attr("src");

        FilmDetailsJson movieItem = new FilmDetailsJson();
        movieItem.setPosterUrl(posterUrl);
        movieItem.setBuyTicketLink(buyTicketUrl);

        Element mainInfo  = info.select(".main-info").first();
        if (mainInfo != null) {
            parseFeatures(info.select(".features"), movieItem);
            String description = info.select(".description").text();
            movieItem.setDescription(description);
        }

        parseImages(info.select(".slider"), movieItem);
        parseTrailers(info.select(".trailer"), movieItem);

        return movieItem;
    }

    private void parseFeatures(Elements src, FilmDetailsJson movieItem) {
        src = src.select(">li");

        for (Element el: src) {
            String key = el.select("label").text().trim().toLowerCase();
            String val = el.select("div").text().trim();
            if (key.contains("старт")) {
                movieItem.setPubDate(dateMapper.map(val));
            } else if (key.contains("страна")) {
                movieItem.setCountry(val);
            } else if (key.contains("режисер")) {
                movieItem.setDirector(val);
            } else if (key.contains("сценарий")) {
                movieItem.setScenario(val);
            } else if (key.contains("в ролях")) {
                movieItem.setActors(val);
            } else if (key.contains("жанр")) {
                movieItem.setGenre(val);
            } else if (key.contains("бюджет")) {
                movieItem.setBudget(val);
            } else if (key.contains("возраст")) {
                movieItem.setAgeLimit(val);
            } else if (key.contains("продолжительность")) {
                movieItem.setDuration(val);
            }
        }
    }

    private void parseImages(Elements src, FilmDetailsJson movieItem) {
        List<ImageUrlJson> urls = new ArrayList<>();
        src = src.select(">a");
        for (Element el: src) {
            String bigImg = el.attr("href");
            String smallImg = el.select("img").attr("src");

            if (smallImg != null && !smallImg.isEmpty()) {
                urls.add(new ImageUrlJson(bigImg, smallImg));
            }
        }

        movieItem.setImageUrls(urls);
    }

    private void parseTrailers(Elements src, FilmDetailsJson movieItem) {
        if (src == null) return;
        List<String> trailerUrls = new ArrayList<>();
        src = src.select(">iframe");
        for (Element el: src) {
            String url = el.attr("src");

            if (url != null && !url.isEmpty()) {
                trailerUrls.add(url);
            }
        }

        movieItem.setTrailerUrls(trailerUrls);
    }
}
