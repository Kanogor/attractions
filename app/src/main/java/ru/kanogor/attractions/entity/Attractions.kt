package ru.kanogor.attractions.entity

interface Attractions {
    val type: String
    val features: List<Features>
}

interface Features {
    val type: String
    val id: String
    val geometry: Geometry
    val properties: Properties
}

interface Geometry {
    val type: String
    val coordinates: List<Double>
}

interface Properties {
    val xid: String
    val name: String
    val dist: Double
    val rate: Int
    val osm: String?
    val wikidata: String?
    val kinds: String
}

