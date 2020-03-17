package com.kinotir.api.mappers

import com.kinotir.api.NewsJson
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

internal class HtmlNewsMapper : Mapper<String?, List<NewsJson>> {

    private val dateMapper = NewsDateMapper()

    override fun map(src: String?): List<NewsJson> {
        return src?.let { Jsoup.parse(it) }
            ?.select(".news .items")?.first()
            ?.select(">li")
            ?.mapNotNull { parseItem(it) }
            ?: emptyList()
    }

    private fun parseItem(src: Element?): NewsJson? {
        src ?: return null
        val info = src.select(">.entry")?.first()
        return NewsJson(
            tag = src.attr("id").trim(),
            title = src.select(">h2").text().trim(),
            body = info?.text()?.trim() ?: "",
            date = dateMapper.map(src.select(">.date").text().trim()),
            imageUrls = info?.select("img")
                ?.mapNotNull { it.attr("src")?.trim() }
                ?: emptyList()
        )
    }
}
