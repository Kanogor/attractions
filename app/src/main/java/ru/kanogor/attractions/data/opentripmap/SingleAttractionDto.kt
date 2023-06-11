package ru.kanogor.attractions.data.opentripmap

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kanogor.attractions.entity.Info
import ru.kanogor.attractions.entity.SingleAttraction

@JsonClass(generateAdapter = true)
data class SingleAttractionDto(
    @Json(name = "xid")
    override val xid: String,
    @Json(name = "name")
    override val name: String,
    @Json(name = "image")
    override val image: String? = null,
    @Json(name = "wikipedia_extracts")
    override val wikipedia_extracts: ru.kanogor.attractions.data.opentripmap.Info? = null
) : SingleAttraction

@JsonClass(generateAdapter = true)
data class Info(
    @Json(name = "text")
    override val text: String
) : Info