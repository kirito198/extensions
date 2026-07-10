package com.kirito198

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import org.jsoup.nodes.Element

class KrmiziProvider : MainAPI() {
    override var mainUrl = "https://krmizi.onl" 
    override var name = "Krmizi"
    override val hasMainPage = true
    override var lang = "ar" 
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Movie)

    override suspend fun getMainPage(page: Int, request: MainActivityLoadRequest): HomePageResponse? {
        val document = app.get(mainUrl).document
        val homeItems = ArrayList<SearchResponse>()
        
        document.select("div.post-item, div.video-item, article").forEach { element ->
            val title = element.select("h2, h3, .title").text().trim()
            val url = element.select("a").attr("href")
            val poster = element.select("img").attr("src")
            
            if (title.isNotEmpty() && url.isNotEmpty()) {
                homeItems.add(newTvSeriesSearchResponse(title, url, TvType.TvSeries) {
                    this.posterUrl = poster
                })
            }
        }
        return newHomePageResponse("المضاف حديثاً", homeItems)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val searchUrl = "$mainUrl/?s=$query"
        val document = app.get(searchUrl).document
        
        return document.select("div.post-item, article").mapNotNull { element ->
            val title = element.select("h2, h3, .title").text().trim()
            val url = element.select("a").attr("href")
            val poster = element.select("img").attr("src")
            
            newTvSeriesSearchResponse(title, url, TvType.TvSeries) {
                this.posterUrl = poster
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        
        document.select("iframe, video source").forEach { element ->
            val videoUrl = element.attr("src").ifEmpty { element.attr("value") }
            if (videoUrl.isNotEmpty()) {
                callback.invoke(
                    ExtractorLink(
                        name,
                        "Krmizi Stream",
                        videoUrl,
                        referer = mainUrl,
                        quality = Qualities.Unknown.value
                    )
                )
            }
        }
        return true
    }
}
