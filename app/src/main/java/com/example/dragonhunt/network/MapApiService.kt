package com.example.dragonhunt.network

import android.os.Build
import com.example.dragonhunt.BuildConfig
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
    private fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
            Build.PRODUCT == "google_sdk"
    }

    private fun baseUrl(): String {
        val host = if (isEmulator()) "10.0.2.2" else BuildConfig.SERVER_HOST
        val port = BuildConfig.SERVER_PORT
        return "http://$host:$port/"
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: MapApiService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MapApiService::class.java)
    }
}
