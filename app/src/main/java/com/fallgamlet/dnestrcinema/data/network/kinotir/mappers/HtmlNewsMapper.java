package com.fallgamlet.dnestrcinema.data.network.kinotir.mappers;

import com.fallgamlet.dnestrcinema.domain.models.NewsItem;
import com.fallgamlet.dnestrcinema.data.network.Mapper;
import com.fallgamlet.dnestrcinema.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class HtmlNewsMapper implements Mapper <String, List<NewsItem>> {

    private NewsDateMapper dateMapper = new NewsDateMapper();

    @Override
    public List<NewsItem> map(String html) {
        if (StringUtils.INSTANCE.isEmpty(html)) {
            return null;
        }

        Document doc = Jsoup.parse(html);
        if (doc == null) {
            return null;
        }

        Element info = doc.select(".news .items").first();
        if (info == null) {
            return null;
        }

        Elements items = info.select(">li");
        if (items == null || items.isEmpty()) {
            return null;
        }

        List<NewsItem> newsItems = new ArrayList<>();

        for (Element item: items) {
            NewsItem newsItem = parseItem(item);
            if (newsItem != null) {
                newsItems.add(newsItem);
            }
        }

        return newsItems;
    }

    private NewsItem parseItem(Element src) {
        if (src == null) {
            return null;
        }

        String tag = src.attr("id").trim();
        String title = src.select(">h2").text().trim();
        String dateStr = src.select(">.date").text().trim();
        String text = null;
        Elements imgs = null;

        Element info = src.select(">.entry").first();
        if (info != null) {
            imgs = info.select("img");
            text = info.text().trim();
        }

        NewsItem newsItem = new NewsItem();
        newsItem.setTag(tag);
        newsItem.setTitle(title);
        newsItem.setBody(text);
        newsItem.setDate(dateMapper.map(dateStr));

        if (imgs != null) {
            for (Element img: imgs) {
                String imgUrl = img.attr("src");
                if (imgUrl != null && !imgUrl.isEmpty()) {
                    imgUrl = imgUrl.trim();
                    newsItem.getImgUrls().add(imgUrl);
                }
            }
        }

        return newsItem;
    }
}
