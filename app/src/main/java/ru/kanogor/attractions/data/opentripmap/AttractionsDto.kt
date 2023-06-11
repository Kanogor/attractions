package ru.kanogor.attractions.data.opentripmap

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kanogor.attractions.entity.Attractions
import ru.kanogor.attractions.entity.Features
import ru.kanogor.attractions.entity.Geometry
import ru.kanogor.attractions.entity.Properties

@JsonClass(generateAdapter = true)
data class AttractionsDto(
    @Json(name = "type")
    override val type: String,
    @Json(name = "features")
    override val features: List<ru.kanogor.attractions.data.opentripmap.Features>
) : Attractions

@JsonClass(generateAdapter = true)
data class Features(
    @Json(name = "type")
    override val type: String,
    @Json(name = "id")
    override val id: String,
    @Json(name = "geometry")
    override val geometry: ru.kanogor.attractions.data.opentripmap.Geometry,
    @Json(name = "properties")
    override val properties: ru.kanogor.attractions.data.opentripmap.Properties
) : Features

@JsonClass(generateAdapter = true)
data class Properties(
    @Json(name = "xid")
    override val xid: String,
    @Json(name = "name")
    override val name: String,
    @Json(name = "dist")
    override val dist: Double,
    @Json(name = "rate")
    override val rate: Int,
    @Json(name = "osm")
    override val osm: String? = null,
    @Json(name = "kinds")
    override val kinds: String,
    @Json(name = "wikidata")
    override val wikidata: String? = null
) : Properties

@JsonClass(generateAdapter = true)
data class Geometry(
    @Json(name = "type")
    override val type: String,
    @Json(name = "coordinates")
    override val coordinates: List<Double>
) : Geometry