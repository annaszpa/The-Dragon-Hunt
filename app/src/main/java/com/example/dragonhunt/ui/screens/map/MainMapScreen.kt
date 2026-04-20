package com.example.dragonhunt.ui.screens.map

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.dragonhunt.model.LocationData
import com.example.dragonhunt.network.RetrofitClient
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@Composable
fun MainMapScreen(
    mapId: String,
    initialLat: Double,
    initialLng: Double,
    onBack: () -> Unit,
    onLocationClick: (String) -> Unit,
    onCollectionClick: () -> Unit
) {
    val context = LocalContext.current
    var locations by remember { mutableStateOf<List<LocationData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val mapView = remember { MapView(context) }

    LaunchedEffect(mapId) {
        try {
            val remoteLocations = RetrofitClient.instance.getLocationsForMap(mapId)
            locations = remoteLocations.map { 
                LocationData(it.id, it.name, it.lat, it.lng, it.unlocked)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    DisposableEffect(Unit) {
        mapView.onCreate(null)
        mapView.onStart()
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                mapView.apply {
                    getMapAsync { map ->
                        map.setStyle(Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")) {
                            map.cameraPosition = CameraPosition.Builder()
                                .target(LatLng(initialLat, initialLng))
                                .zoom(15.0)
                                .build()

                            if (locations.isNotEmpty()) {
                                addMarkersToMap(map, locations, onLocationClick)
                            }
                        }
                    }
                }
            },
            update = { view ->
                view.getMapAsync { map ->
                    val style = map.style
                    if (style != null && style.isFullyLoaded && locations.isNotEmpty()) {
                        addMarkersToMap(map, locations, onLocationClick)
                    }
                }
            }
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFFD4AF37)
            )
        }

        MapOverlayButton(
            text = "← BACK",
            onClick = onBack,
            modifier = Modifier.statusBarsPadding().align(Alignment.TopStart)
        )

        MapOverlayButton(
            text = "COLLECTION",
            onClick = onCollectionClick,
            modifier = Modifier.statusBarsPadding().align(Alignment.TopEnd)
        )
    }
}

private fun addMarkersToMap(
    map: org.maplibre.android.maps.MapLibreMap,
    locations: List<LocationData>,
    onLocationClick: (String) -> Unit
) {
    map.clear()
    locations.forEach { loc ->
        map.addMarker(MarkerOptions()
            .position(LatLng(loc.lat, loc.lng))
            .title(loc.name))
    }
    map.setOnMarkerClickListener { marker ->
        locations.find { it.name == marker.title }?.let { onLocationClick(it.id) }
        true
    }

}
