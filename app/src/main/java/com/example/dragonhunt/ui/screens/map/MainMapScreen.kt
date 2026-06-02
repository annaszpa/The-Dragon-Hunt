package com.example.dragonhunt.ui.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style

@SuppressLint("MissingPermission")
@Composable
fun MainMapScreen(
    mapId: String,
    onBack: () -> Unit,
    onLocationClick: (LocationData) -> Unit,
    onCollectionClick: () -> Unit
) {
    val context = LocalContext.current
    var locations by remember { mutableStateOf<List<LocationData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val mapView = remember { MapView(context) }
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var mapInstance by remember { mutableStateOf<MapLibreMap?>(null) }
    var isStyleLoaded by remember { mutableStateOf(false) }
    var hasLocationPermission by remember {
        mutableStateOf(
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasLocationPermission = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }
    val fallbackCenter = LatLng(50.0619, 19.9368)

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(mapId) {
        try {
            val remoteLocations = RetrofitClient.instance.getLocationsForMap(mapId)
            locations = remoteLocations.map { 
                LocationData(it.id, it.name, it.lat, it.lng, it.description, it.unlocked)
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

    DisposableEffect(hasLocationPermission) {
        if (!hasLocationPermission) {
            return@DisposableEffect onDispose { }
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L)
            .setMinUpdateDistanceMeters(2f)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    currentLocation = location
                }
            }
        }

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                context.mainLooper
            )
        } catch (_: SecurityException) {
        }

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    LaunchedEffect(currentLocation, mapInstance) {
        val loc = currentLocation ?: return@LaunchedEffect
        val map = mapInstance ?: return@LaunchedEffect
        val userLatLng = LatLng(loc.latitude, loc.longitude)
        map.animateCamera(CameraUpdateFactory.newLatLng(userLatLng))
    }

    LaunchedEffect(isStyleLoaded, locations, currentLocation, mapInstance) {
        if (!isStyleLoaded || mapInstance == null) return@LaunchedEffect
        val userLatLng = currentLocation?.let { LatLng(it.latitude, it.longitude) }
        addMarkersToMap(context, mapInstance!!, locations, userLatLng, onLocationClick)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                mapView.apply {
                    getMapAsync { map ->
                        mapInstance = map
                        map.uiSettings.isCompassEnabled = false
                        map.setStyle(Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")) {
                            isStyleLoaded = true
                            map.cameraPosition = CameraPosition.Builder()
                                .target(fallbackCenter)
                                .zoom(13.5)
                                .build()
                        }
                    }
                }
            },
            update = { view ->
                view.getMapAsync { map ->
                    mapInstance = map
                    map.uiSettings.isCompassEnabled = false
                    val style = map.style
                    if (style != null && style.isFullyLoaded) {
                        if (!isStyleLoaded) {
                            isStyleLoaded = true
                        }
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

        MapOverlayButton(
            text = "MY LOCATION",
            onClick = {
                val loc = currentLocation ?: return@MapOverlayButton
                val map = mapInstance ?: return@MapOverlayButton
                val userLatLng = LatLng(loc.latitude, loc.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16.0))
            },
            modifier = Modifier.navigationBarsPadding().align(Alignment.BottomEnd)
        )
    }
}
