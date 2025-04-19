package com.kinotir.api.mappers

import com.kinotir.api.FilmJson
import com.kinotir.api.FilmSessionJson
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

internal class HtmlFilmsMapper : Mapper<String?, List<FilmJson>> {
    private val dateMapper = MovieDateMapper()

    override fun map(src: String?): List<FilmJson> {
        return src?.let { Jsoup.parse(it) }
            ?.select(".movies-tile>.item>.item-wrapper")
            ?.mapNotNull { parseElement(it) }
            ?: emptyList()
    }

    private fun parseElement(element: Element?): FilmJson? {
        element ?: return null
        val movieItem = FilmJson(
            title = element.select(">h2")?.text(),
            posterUrl = element.select(">.poster>img")?.attr("src"),
            link = element.select(">.overlay>a")?.attr("href"),
            buyTicketLink = element.select(">.overlay>.by-ticket>a")?.attr("href")
        )
        parseFeatures(element.select(">.overlay .features"), movieItem)
        parseSchedule(element.select(">.overlay .halls>li"), movieItem)
        parseTrailers(element.select(">.overlay>.links>a.trailer"), movieItem)
        return movieItem
    }

    private fun parseFeatures(src: Elements?, movieItem: FilmJson) {
        src?.select(">li")
            ?.mapNotNull {el ->
                val value = if (el.childNodeSize() >= 2) el.childNode(1)?.toString()?.trim()
                else null
                value ?.let { Pair(el, it) }
            }
            ?.forEach { pair ->
                val el = pair.first
                val value = pair.second
                when {
                    el.select(">i:containsOwn(Старт)").isNotEmpty() ->
                        movieItem.pubDate = dateMapper.map(value)
                    el.select("i:containsOwn(Жанр)").isNotEmpty() ->
                        movieItem.genre = value
                    el.select("i:containsOwn(Возраст)").isNotEmpty() ->
                        movieItem.ageLimit = value
                    el.select("i:containsOwn(Продолжительность)").isNotEmpty() ->
                        movieItem.duration = value
                }
            }
    }

    private fun parseSchedule(src: Elements?, movieItem: FilmJson) {
        movieItem.sessions = src?.mapNotNull { parseScheduleItem(it) }
    }

    private fun parseScheduleItem(item: Element?): FilmSessionJson? {
        item ?: return null

        val last = item.select(">i")?.last()
        val index = last?.let { item.childNodes()?.indexOf(it) } ?: -1
        val elTime = item.childNodes().getOrNull(index+1)

        return FilmSessionJson(
            room = item.select(">h5")?.text(),
            value = elTime?.toString()
        )
    }

    private fun parseTrailers(src: Elements?, movieItem: FilmJson) {
        movieItem.trailerUrls = src
            ?.mapNotNull { it.attr("href") }
            ?.filter { it.isNotBlank() }
    }
}
