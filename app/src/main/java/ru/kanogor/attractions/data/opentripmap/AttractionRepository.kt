package ru.kanogor.attractions.data.opentripmap

import ru.kanogor.attractions.entity.Attractions
import ru.kanogor.attractions.entity.SingleAttraction

class AttractionRepository(private val searchAttractions: SearchAttractions) {

    suspend fun getAttractions(radius: Int, longitude: Double, latitude: Double): Attractions {
        val getApi = searchAttractions.getAttractions(radius, longitude, latitude)
        return getApi.body()!!
    }

    suspend fun getSingleAttraction(xid: String): SingleAttraction {
        val getInfo = searchAttractions.getXid(xid)
        return getInfo.body()!!
    }
}