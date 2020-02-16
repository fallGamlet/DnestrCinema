package com.kinotir.api.mappers;

import com.kinotir.api.NewsJson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

class HtmlNewsMapper implements Mapper<String, List<NewsJson>> {

    private NewsDateMapper dateMapper = new NewsDateMapper();

    @Override
    public List<NewsJson> map(String html) {
        if (html == null || html.isEmpty()) return null;

        Document doc = Jsoup.parse(html);
        if (doc == null) return null;

        Element info = doc.select(".news .items").first();
        if (info == null) return null;

        Elements items = info.select(">li");
        if (items == null || items.isEmpty()) return null;

        List<NewsJson> newsItems = new ArrayList<>();

        for (Element item: items) {
            NewsJson newsItem = parseItem(item);
            if (newsItem != null) {
                newsItems.add(newsItem);
            }
        }

        return newsItems;
    }

    private NewsJson parseItem(Element src) {
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

        List<String> imageUrls = new ArrayList<>();
        if (imgs != null) {
            for (Element img: imgs) {
                String imgUrl = img.attr("src");
                if (imgUrl != null && !imgUrl.isEmpty()) {
                    imgUrl = imgUrl.trim();
                    imageUrls.add(imgUrl);
                }
            }
        }

        NewsJson newsItem = new NewsJson();
        newsItem.setTag(tag);
        newsItem.setTitle(title);
        newsItem.setBody(text);
        newsItem.setDate(dateMapper.map(dateStr));
        newsItem.setImageUrls(imageUrls);

        return newsItem;
    }
}
