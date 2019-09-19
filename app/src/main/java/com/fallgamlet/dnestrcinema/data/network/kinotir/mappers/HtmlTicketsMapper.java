package com.fallgamlet.dnestrcinema.data.network.kinotir.mappers;

import com.fallgamlet.dnestrcinema.data.network.Mapper;
import com.fallgamlet.dnestrcinema.domain.models.TicketItem;
import com.fallgamlet.dnestrcinema.domain.models.TicketPlace;
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

public class HtmlTicketsMapper implements Mapper<String, List<TicketItem>> {
    @Override
    public List<TicketItem> map(String html) {
        if (StringUtils.INSTANCE.isEmpty(html)) {
            return null;
        }

        Document doc = Jsoup.parse(html);
        if (doc == null) {
            return null;
        }

        Elements items = doc.select(".orders .items > .item");
        if (items == null) {
            return null;
        }

        List<TicketItem> tickets = new ArrayList<>();

        for (Element item: items) {
            TicketItem ticketItem = parseItem(item);
            if (ticketItem != null) {
                tickets.add(ticketItem);
            }
        }

        return tickets;
    }

    private TicketItem parseItem(Element item) {
        if (item == null) {
            return null;
        }

        TicketItem ticketItem = new TicketItem();

        Element linkA = item.select("h2>a").first();
        Elements infoItems = item.select(".order-data > li");
        Elements places = item.select(".tickets > li");

        ticketItem.setTitle(linkA.text());
        ticketItem.setUrl(linkA.attr("href"));

        parseTicketInfo(infoItems, ticketItem);

        List<TicketPlace> ticketPlaces = parseTicketPlaces(places);
        ticketItem.setTicketPlaceSet(ticketPlaces);

        return ticketItem;
    }

    private void parseTicketInfo(Elements infoItems, TicketItem ticketItem) {
        if (ticketItem == null) {
            return;
        }

        String order = null;
        String room = null;
        String status = null;
        String dateStr = null;
        String timeStr = null;

        for (Element el: infoItems) {
            String val = el.childNodeSize() >= 2? el.childNode(1).toString().trim(): null;
            if (val != null) {
                if (!el.select(">b:containsOwn(Заказ)").isEmpty()) {
                    order = val;
                } else if (!el.select(">b:containsOwn(Зал кинотеатра)").isEmpty()) {
                    room = val;
                } else if (!el.select(">b:containsOwn(Статус)").isEmpty()) {
                    status = val;
                } else if (!el.select(">b:containsOwn(Дата сеанса)").isEmpty()) {
                    dateStr = val;
                } else if (!el.select(">b:containsOwn(Время сеанса)").isEmpty()) {
                    timeStr = val;
                }
            }
        }

        ticketItem.setId(order);
        ticketItem.setRoom(room);
        ticketItem.setStatus(status);
        ticketItem.setDate(dateStr);
        ticketItem.setTime(timeStr);

    }

    private List<TicketPlace> parseTicketPlaces(Elements items) {
        List<TicketPlace> ticketPlaces = new ArrayList<>();
        if (items == null || items.isEmpty()) {
            return ticketPlaces;
        }

        for (Element item: items) {
            TicketPlace ticketPlace = parseTicketPlace(item);

            if (ticketPlace != null) {
                ticketPlaces.add(ticketPlace);
            }
        }

        return ticketPlaces;
    }

    private TicketPlace parseTicketPlace(Element item) {
        if (item == null) {
            return null;
        }

        TicketPlace place = new TicketPlace();

        Element img = item.select("div.code > img").first();
        Elements infoItems = item.select(".additional > ul > li");

        place.setUrl(img.attr("src"));

        parseTicketPlaceInfo(infoItems, place);

        return place;
    }

    private void parseTicketPlaceInfo(Elements infoItems, TicketPlace ticketPlace) {
        if (infoItems == null || infoItems.isEmpty()) {
            return;
        }

        String row = null;
        String place = null;

        for (Element el: infoItems) {
            if (el.childNodeSize() >= 2) {
                String val = el.childNode(1).toString().trim();

                if (!el.select(">b:containsOwn(Ряд)").isEmpty()) {
                    row = val;
                } else if (!el.select(">b:containsOwn(Место)").isEmpty()) {
                    place = val;
                }
            }
        }

        ticketPlace.setRow(Integer.parseInt(row, 10));
        ticketPlace.setPlace(Integer.parseInt(place, 10));
    }
}
