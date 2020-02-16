package com.kinotir.api.mappers;

import com.kinotir.api.TicketJson;
import com.kinotir.api.TicketPlaceJson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

class HtmlTicketsMapper implements Mapper<String, List<TicketJson>> {

    @Override
    public List<TicketJson> map(String html) {
        if (html == null || html.isEmpty()) return null;

        Document doc = Jsoup.parse(html);
        if (doc == null) return null;

        Elements items = doc.select(".orders .items > .item");
        if (items == null) return null;

        List<TicketJson> tickets = new ArrayList<>();

        for (Element item: items) {
            TicketJson ticketItem = parseItem(item);
            if (ticketItem != null) {
                tickets.add(ticketItem);
            }
        }

        return tickets;
    }

    private TicketJson parseItem(Element item) {
        if (item == null) return null;

        Element linkA = item.select("h2>a").first();
        Elements infoItems = item.select(".order-data > li");
        Elements places = item.select(".tickets > li");

        TicketJson ticketItem = new TicketJson();
        ticketItem.setTitle(linkA.text());
        ticketItem.setUrl(linkA.attr("href"));

        parseTicketInfo(infoItems, ticketItem);

        List<TicketPlaceJson> ticketPlaces = parseTicketPlaces(places);
        ticketItem.setTicketPlaces(ticketPlaces);

        return ticketItem;
    }

    private void parseTicketInfo(Elements infoItems, TicketJson ticketItem) {
        if (ticketItem == null) return;

        for (Element el: infoItems) {
            String val = el.childNodeSize() >= 2? el.childNode(1).toString().trim(): null;
            if (val != null) {
                if (!el.select(">b:containsOwn(Заказ)").isEmpty()) {
                    ticketItem.setId(val);
                } else if (!el.select(">b:containsOwn(Зал кинотеатра)").isEmpty()) {
                    ticketItem.setRoom(val);
                } else if (!el.select(">b:containsOwn(Статус)").isEmpty()) {
                    ticketItem.setStatus(val);
                } else if (!el.select(">b:containsOwn(Дата сеанса)").isEmpty()) {
                    ticketItem.setDate(val);
                } else if (!el.select(">b:containsOwn(Время сеанса)").isEmpty()) {
                    ticketItem.setTime(val);
                }
            }
        }
    }

    private List<TicketPlaceJson> parseTicketPlaces(Elements items) {
        List<TicketPlaceJson> ticketPlaces = new ArrayList<>();
        if (items == null || items.isEmpty()) return ticketPlaces;

        for (Element item: items) {
            TicketPlaceJson ticketPlace = parseTicketPlace(item);

            if (ticketPlace != null) {
                ticketPlaces.add(ticketPlace);
            }
        }

        return ticketPlaces;
    }

    private TicketPlaceJson parseTicketPlace(Element item) {
        if (item == null) return null;

        Element img = item.select("div.code > img").first();
        Elements infoItems = item.select(".additional > ul > li");

        TicketPlaceJson place = new TicketPlaceJson();
        place.setUrl(img.attr("src"));
        parseTicketPlaceInfo(infoItems, place);

        return place;
    }

    private void parseTicketPlaceInfo(Elements infoItems, TicketPlaceJson ticketPlace) {
        if (infoItems == null || infoItems.isEmpty()) return;

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

        ticketPlace.setRow(row);
        ticketPlace.setPlace(place);
    }
}
