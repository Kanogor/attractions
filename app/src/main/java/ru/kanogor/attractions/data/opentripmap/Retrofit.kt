package ru.kanogor.attractions.data.opentripmap

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

private const val BASE_URL = "https://api.opentripmap.com/0.1/ru/"

//  https://api.opentripmap.com/0.1/ru/places/radius?radius=3000&lon=44.634032&lat=48.821549&apikey=5ae2e3f221c38a28845f05b6cbfa778609261579e11e58f36e5e10c0
@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideSearchAttractions(retrofit: Retrofit): SearchAttractions =
        retrofit.create(SearchAttractions::class.java)

    @Singleton
    @Provides
    fun provideAttractionsRepository(searchAttractions: SearchAttractions) =
        AttractionRepository(searchAttractions)

}

interface SearchAttractions {

    @GET("places/radius")
    suspend fun getAttractions(
        @Query("radius") radius: Int,
        @Query("lon") longitude: Double,
        @Query("lat") latitude: Double,
        @Query("apikey") apikey: String = API_KEY,
    ): Response<AttractionsDto>

    @GET("places/xid/{xid}")
    suspend fun getXid(
        @Path("xid") xid: String,
        @Query("apikey") apikey: String = API_KEY,
    ): Response<SingleAttractionDto>

    private companion object {
        private const val API_KEY = "5ae2e3f221c38a28845f05b6cbfa778609261579e11e58f36e5e10c0"
    }
}



