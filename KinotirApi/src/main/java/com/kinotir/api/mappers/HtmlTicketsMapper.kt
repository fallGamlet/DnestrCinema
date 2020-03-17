package com.kinotir.api.mappers

import com.kinotir.api.TicketJson
import com.kinotir.api.TicketPlaceJson
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

internal class HtmlTicketsMapper : Mapper<String?, List<TicketJson>> {

    override fun map(src: String?): List<TicketJson> {
        return src?.let { Jsoup.parse(it) }
            ?.select(".orders .items > .item")
            ?.mapNotNull { parseItem(it) }
            ?: emptyList()
    }

    private fun parseItem(item: Element?): TicketJson? {
        item ?: return null
        val linkA = item.select("h2>a")?.first()
        val ticketItem = TicketJson(
            title = linkA?.text(),
            url = linkA?.attr("href"),
            ticketPlaces = parseTicketPlaces(item.select(".tickets > li"))
        )
        parseTicketInfo(item.select(".order-data > li"), ticketItem)
        return ticketItem
    }

    private fun parseTicketInfo(infoItems: Elements?, ticketItem: TicketJson) {
        infoItems
            ?.mapNotNull { el ->
                val value = if (el.childNodeSize() >= 2) el.childNode(1).toString().trim()
                else null

                value?.let { Pair(el, it) }
            }
            ?.forEach {
                val el = it.first
                val value = it.second
                when {
                    el.select(">b:containsOwn(Заказ)").isNotEmpty() ->
                        ticketItem.id = value
                    el.select(">b:containsOwn(Зал кинотеатра)").isNotEmpty() ->
                        ticketItem.room = value
                    el.select(">b:containsOwn(Статус)").isNotEmpty() ->
                        ticketItem.status = value
                    el.select(">b:containsOwn(Дата сеанса)").isNotEmpty() ->
                        ticketItem.date = value
                    el.select(">b:containsOwn(Время сеанса)").isNotEmpty() ->
                        ticketItem.time = value
                }
            }
    }

    private fun parseTicketPlaces(items: Elements?): List<TicketPlaceJson> {
        return items?.mapNotNull { parseTicketPlace(it) }
            ?: emptyList()
    }

    private fun parseTicketPlace(item: Element?): TicketPlaceJson? {
        item ?: return null
        val place = TicketPlaceJson(
            url = item.select("div.code > img")?.first()?.attr("src")
        )
        parseTicketPlaceInfo(item.select(".additional > ul > li"), place)
        return place
    }

    private fun parseTicketPlaceInfo(infoItems: Elements?, ticketPlace: TicketPlaceJson) {
        if (infoItems == null || infoItems.isEmpty()) return
        var row: String? = null
        var place: String? = null
        infoItems.forEach { el ->
            if (el.childNodeSize() >= 2) {
                val value = el.childNode(1).toString().trim()
                when {
                    el.select(">b:containsOwn(Ряд)").isNotEmpty() -> row = value
                    el.select(">b:containsOwn(Место)").isNotEmpty() -> place = value
                }
            }
        }
        ticketPlace.row = row
        ticketPlace.place = place
    }
}
