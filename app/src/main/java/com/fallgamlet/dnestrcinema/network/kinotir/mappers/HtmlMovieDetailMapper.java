package com.fallgamlet.dnestrcinema.network.kinotir.mappers;

import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.network.Mapper;
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
            movieItem.setDescription(description);
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
            String key = el.select("label").text().trim();
            String val = el.select("div").text().trim();
            if (val != null) {
                if ("Старт:".equals(key)) {
                    strStart = val;
                } else if ("Страна:".equals(key)) {
                    strCountry = val;
                } else if ("Режисер:".equals(key)) {
                    strDirector= val;
                } else if ("Сценарий:".equals(key)) {
                    strScenario= val;
                } else if ("В ролях:".equals(key)) {
                    strActors = val;
                } else if ("Жанр:".equals(key)) {
                    strGenre = val;
                } else if ("Бюджет:".equals(key)) {
                    strBudget = val;
                } else if ("Возраст:".equals(key)) {
                    strAgeLimit = val;
                } else if ("Продолжительность:".equals(key)) {
                    strDuration = val;
                }
            }
        }

        Date start = dateMapper.map(strStart);

        movieItem.setPubDate(start);
        movieItem.setCountry(strCountry);
        movieItem.setDirector(strDirector);
        movieItem.setScenario(strScenario);
        movieItem.setActors(strActors);
        movieItem.setGenre(strGenre);
        movieItem.setBudget(strBudget);
        movieItem.setAgeLimit(strAgeLimit);
        movieItem.setDuration(strDuration);
    }

    private void parseImages(Elements src, MovieItem movieItem) {
        if (src == null || movieItem == null) {
            return;
        }

        src = src.select(">a");
        for (Element el: src) {
            String bigImg = el.attr("href");
            String smallImg = el.select("img").attr("src");

            if (smallImg != null && !smallImg.isEmpty()) {
                movieItem.getImgUrlSet().add(smallImg);
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

}
