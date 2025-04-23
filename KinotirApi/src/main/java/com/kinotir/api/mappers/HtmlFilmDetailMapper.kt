package com.kinotir.api.mappers

import com.kinotir.api.FilmDetailsJson
import com.kinotir.api.ImageUrlJson
import org.jsoup.Jsoup
import org.jsoup.select.Elements

internal class HtmlFilmDetailMapper : Mapper<String?, FilmDetailsJson?> {

    private val dateMapper = MovieDateMapper()

    override fun map(src: String?): FilmDetailsJson? {
        val doc = src?.let { Jsoup.parse(it) } ?: return null
        val info = doc.select(".film .info").first() ?: return null

        val result = FilmDetailsJson(
            title = doc.select(".film h1 span")?.text(),
            posterUrl = info.select(">.additional-poster>.image>img")?.attr("src"),
            buyTicketLink = doc.select("a.buy-ticket-btn").attr("href"),
            description = info.select(".description")?.text()
        )
        parseFeatures(info.select(".features"), result)
        parseImages(info.select(".slider"), result)
        parseTrailers(info.select(".trailer"), result)
        return result
    }

    private fun parseFeatures(src: Elements?, destination: FilmDetailsJson) {
        src?.select(">li")
            ?.forEach {
                val key = it.select("label")?.text()?.trim()?.lowercase() ?: ""
                val value = it.select("div")?.text()?.trim() ?: ""
                when {
                    key.contains("старт") -> destination.pubDate = dateMapper.map(value)
                    key.contains("страна") -> destination.country = value
                    key.contains("режисер") -> destination.director = value
                    key.contains("сценарий") -> destination.scenario = value
                    key.contains("в ролях") -> destination.actors = value
                    key.contains("жанр") -> destination.genre = value
                    key.contains("бюджет") -> destination.budget = value
                    key.contains("возраст") -> destination.ageLimit = value
                    key.contains("продолжительность") -> destination.duration = value
                }
            }
    }

    private fun parseImages(src: Elements?, destination: FilmDetailsJson) {
        destination.imageUrls = src?.select(">a")
            ?.mapNotNull {
                val bigImg = it.attr("href") ?: ""
                val smallImg = it.select("img")?.attr("src") ?: ""
                if (smallImg.isBlank()) null
                else ImageUrlJson(bigImg, smallImg)
            }
            ?: emptyList()
    }

    private fun parseTrailers(src: Elements?, destination: FilmDetailsJson) {
        destination.trailerUrls = src?.select(">iframe")
            ?.mapNotNull { it.attr("src") }
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }
}
