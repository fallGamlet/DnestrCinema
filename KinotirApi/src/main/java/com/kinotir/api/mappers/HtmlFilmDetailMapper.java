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

        FilmDetailsJson result = new FilmDetailsJson();
        result.setPosterUrl(posterUrl);
        result.setBuyTicketLink(buyTicketUrl);

        Element mainInfo  = info.select(".main-info").first();
        if (mainInfo != null) {
            parseFeatures(info.select(".features"), result);
            String description = info.select(".description").text();
            result.setDescription(description);
        }

        parseImages(info.select(".slider"), result);
        parseTrailers(info.select(".trailer"), result);

        return result;
    }

    private void parseFeatures(Elements src, FilmDetailsJson destination) {
        src = src.select(">li");

        for (Element el: src) {
            String key = el.select("label").text().trim().toLowerCase();
            String val = el.select("div").text().trim();
            if (key.contains("старт")) {
                destination.setPubDate(dateMapper.map(val));
            } else if (key.contains("страна")) {
                destination.setCountry(val);
            } else if (key.contains("режисер")) {
                destination.setDirector(val);
            } else if (key.contains("сценарий")) {
                destination.setScenario(val);
            } else if (key.contains("в ролях")) {
                destination.setActors(val);
            } else if (key.contains("жанр")) {
                destination.setGenre(val);
            } else if (key.contains("бюджет")) {
                destination.setBudget(val);
            } else if (key.contains("возраст")) {
                destination.setAgeLimit(val);
            } else if (key.contains("продолжительность")) {
                destination.setDuration(val);
            }
        }
    }

    private void parseImages(Elements src, FilmDetailsJson destination) {
        List<ImageUrlJson> urls = new ArrayList<>();
        src = src.select(">a");
        for (Element el: src) {
            String bigImg = el.attr("href");
            String smallImg = el.select("img").attr("src");

            if (smallImg != null && !smallImg.isEmpty()) {
                urls.add(new ImageUrlJson(bigImg, smallImg));
            }
        }

        destination.setImageUrls(urls);
    }

    private void parseTrailers(Elements src, FilmDetailsJson destination) {
        if (src == null) return;
        List<String> trailerUrls = new ArrayList<>();
        src = src.select(">iframe");
        for (Element el: src) {
            String url = el.attr("src");

            if (url != null && !url.isEmpty()) {
                trailerUrls.add(url);
            }
        }

        destination.setTrailerUrls(trailerUrls);
    }
}
