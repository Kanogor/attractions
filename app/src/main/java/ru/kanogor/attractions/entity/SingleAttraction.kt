package ru.kanogor.attractions.entity

interface SingleAttraction {
    val xid: String
    val name: String
    val image: String?
    val wikipedia_extracts: Info?
}

interface Info {
    val text: String
}
