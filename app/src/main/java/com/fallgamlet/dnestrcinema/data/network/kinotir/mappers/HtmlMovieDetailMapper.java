package com.fallgamlet.dnestrcinema.data.network.kinotir.mappers;

import com.fallgamlet.dnestrcinema.domain.models.MovieDetailItem;
import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.data.network.Mapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class HtmlMovieDetailMapper implements Mapper<String, MovieItem> {

    private MovieDateMapper dateMapper = new MovieDateMapper();

    @Override
    public MovieItem map(String html) {
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

        MovieItem movieItem = new MovieItem();
        movieItem.setPosterUrl(posterUrl);
        movieItem.setBuyTicketLink(buyTicketUrl);

        Element mainInfo  = info.select(".main-info").first();
        if (mainInfo != null) {
            parseFeatures(info.select(".features"), movieItem);
            String description = info.select(".description").text(); // span

            MovieDetailItem detailItem = getDetail(movieItem);
            detailItem.setDescription(description);
        }

        parseImages(info.select(".slider"), movieItem);
        parseTrailers(info.select(".trailer"), movieItem);

        return movieItem;
    }

    private void parseFeatures(Elements src, MovieItem movieItem) {
        if (src == null || movieItem == null) {
            return;
        }

        src = src.select(">li");

        String strStart = null;
        String strCountry = null;
        String strDirector = null;
        String strScenario = null;
        String strActors = null;
        String strGenre = null;
        String strBudget = null;
        String strAgeLimit = null;
        String strDuration = null;

        for (Element el: src) {
            String key = el.select("label").text().trim().toLowerCase();
            String val = el.select("div").text().trim();
            if (key != null && val != null) {
                if (key.contains("старт")) {
                    strStart = val;
                } else if (key.contains("страна")) {
                    strCountry = val;
                } else if (key.contains("режисер")) {
                    strDirector= val;
                } else if (key.contains("сценарий")) {
                    strScenario= val;
                } else if (key.contains("в ролях")) {
                    strActors = val;
                } else if (key.contains("жанр")) {
                    strGenre = val;
                } else if (key.contains("бюджет")) {
                    strBudget = val;
                } else if (key.contains("возраст")) {
                    strAgeLimit = val;
                } else if (key.contains("продолжительность")) {
                    strDuration = val;
                }
            }
        }

        Date start = dateMapper.map(strStart);

        MovieDetailItem detail = getDetail(movieItem);

        movieItem.setPubDate(start);
        movieItem.setDuration(strDuration);

        detail.setCountry(strCountry);
        detail.setDirector(strDirector);
        detail.setScenario(strScenario);
        detail.setActors(strActors);
        detail.setGenre(strGenre);
        detail.setBudget(strBudget);
        detail.setAgeLimit(strAgeLimit);

    }

    private void parseImages(Elements src, MovieItem movieItem) {
        if (src == null || movieItem == null) {
            return;
        }

        MovieDetailItem detail = getDetail(movieItem);

        src = src.select(">a");
        for (Element el: src) {
            String bigImg = el.attr("href");
            String smallImg = el.select("img").attr("src");

            if (smallImg != null && !smallImg.isEmpty()) {
                detail.getImgUrls().add(smallImg);
            }
        }
    }

    private void parseTrailers(Elements src, MovieItem movieItem) {
        if (src == null || movieItem == null) {
            return;
        }

        src = src.select(">iframe");
        for (Element el: src) {
            String url = el.attr("src");

            if (url != null && !url.isEmpty()) {
                movieItem.getTrailerUrlSet().add(url);
            }
        }
    }

    private MovieDetailItem getDetail(MovieItem movieItem) {
        MovieDetailItem detail = movieItem.getDetail();
        if (detail == null) {
            detail = new MovieDetailItem();
            movieItem.setDetail(detail);
        }

        return detail;
    }
}
