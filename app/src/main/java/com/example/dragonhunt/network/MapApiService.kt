package com.example.dragonhunt.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

data class RemoteLocation(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val unlocked: Boolean = false
)

data class RemoteMap(
    val id: String,
    val name: String,
    val description: String,
    val dragonsCount: Int,
    val centerLat: Double,
    val centerLng: Double
)

interface MapApiService {
    @GET("maps")
    suspend fun getMaps(): List<RemoteMap>

    @GET("maps/{mapId}/locations")
    suspend fun getLocationsForMap(@Path("mapId") mapId: String): List<RemoteLocation>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: MapApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MapApiService::class.java)
    }
}
