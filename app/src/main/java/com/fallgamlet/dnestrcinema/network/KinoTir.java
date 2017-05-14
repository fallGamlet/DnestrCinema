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
    public static final String BASE_URL = "http://kinotir.md";
    public static final String PATH_NOW = "/";
    public static final String PATH_SOON = "/skoro-v-kino";
    public static final String PATH_NEWS = "/novosti";
    public static final String PATH_ROOMS = "/zaly-kinoteatra";
    public static final String PATH_QA = "/vopros-otvet";
    public static final String PATH_ABOUT = "/o-kinoteatre";

    public static final String PATH_IMG_ROOM_BLUE = "/files/uploads/img_03.jpg";
    public static final String PATH_IMG_ROOM_BORDO = "/files/uploads/img_07.jpg";
    public static final String PATH_IMG_ROOM_DVD = "/files/uploads/img_11.jpg";


    public interface Parser<T> {
        T parse(String body);
    }

    private static Date parseDate(String val) {
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

    public static class MovieDetailParser implements Parser<MovieItem> {

        @Override
        public MovieItem parse(String html) {
            if (html == null) {return null;}

            Document doc = Jsoup.parse(html);
            if (doc == null) { return null;}

            String buyTicketUrl = doc.select("a.buy-ticket-btn").attr("href");

            Element info = doc.select(".film .info").first();
            if (info == null) {return null;}

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

//            <ul class="features">
//                <li> <label>Старт:</label> <div>с 16 марта, 2017</div> </li>
//                <li> <label>Страна: </label> <div>США</div> </li>
//                <li> <label>Режисер: </label> <div>М. Найт Шьямалан</div> </li>
//                <li> <label>Сценарий: </label> <div>М. Найт Шьямалан</div> </li>
//                <li> <label>В ролях: </label> <div>Джеймс МакЭвой, Аня Тейлор-Джой, Бетти Бакли, Хейли Лу Ричардсон, Джессика Сула, Иззи Коффи, Брэд Уильям Хенке, Себастьян Арселус, Нил Хафф, Уки Вашингтон</div> </li>
//                <li> <label>Жанр: </label> <div>ужасы, триллер</div> </li>
//                <li> <label>Бюджет: </label> <div>$9 000 000</div> </li>
//                <li> <label>Возраст: </label> <div>16+</div> </li>
//                <li> <label>Продолжительность: </label> <div>117 минут</div> </li>
//            </ul>

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

            Date start = parseDate(strStart);

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

    public static class NewsParser implements Parser<List<NewsItem>> {

        @Override
        public List<NewsItem> parse(String html) {
            if (html == null) {return null;}

            Document doc = Jsoup.parse(html);
            if (doc == null) { return null;}

            Element info = doc.select(".news .items").first();
            if (info == null) {return null;}

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
            newsItem.setDate(parseDate(dateStr));

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

        private Date parseDate(String dateStr) {
            if (dateStr == null) {
                return null;
            }
            dateStr = dateStr.trim();
            if (dateStr.isEmpty()) {
                return null;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            Date date;
            try {
                date = formatter.parse(dateStr);
            } catch (Exception ignored) {
                date = null;
            }
            return date;
        }
    }
}
