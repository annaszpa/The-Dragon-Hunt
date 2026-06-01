package com.example.dragonhunt.ui.screens.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.location.Location
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.dragonhunt.model.LocationData
import org.maplibre.android.annotations.IconFactory
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap

private const val ACTIVATION_RADIUS_METERS = 100.0

internal fun addMarkersToMap(
    context: android.content.Context,
    map: MapLibreMap,
    locations: List<LocationData>,
    userLocation: LatLng?,
    onLocationClick: (LocationData) -> Unit
) {
    map.clear()

    val density = context.resources.displayMetrics.density
    val pinSize = 20f * density
    val nearIcon = IconFactory.getInstance(context).fromBitmap(
        createPinBitmap(Color(0xFFD32F2F).toArgb(), pinSize)
    )
    val farIcon = IconFactory.getInstance(context).fromBitmap(
        createPinBitmap(Color(0xFF9E9E9E).toArgb(), pinSize)
    )

    locations.forEach { loc ->
        val distance = userLocation?.let { distanceMeters(it, loc) }
        val isNear = distance != null && distance <= ACTIVATION_RADIUS_METERS
        map.addMarker(
            MarkerOptions()
                .position(LatLng(loc.lat, loc.lng))
                .title(loc.name)
                .icon(if (isNear) nearIcon else farIcon)
        )
    }

    userLocation?.let {
        val emojiBitmap = createEmojiBitmap("\uD83D\uDD75\uFE0F", 28f * density)
        val icon = IconFactory.getInstance(context).fromBitmap(emojiBitmap)
        map.addMarker(
            MarkerOptions()
                .position(it)
                .icon(icon)
        )
    }

    map.setOnMarkerClickListener { marker ->
        val location = locations.find { it.name == marker.title } ?: return@setOnMarkerClickListener true
        val distance = userLocation?.let { distanceMeters(it, location) }
        val isNear = distance != null && distance <= ACTIVATION_RADIUS_METERS
        if (isNear) {
            onLocationClick(location)
        } else {
            Toast.makeText(
                context,
                "Move closer to activate the egg (within 100 m).",
                Toast.LENGTH_SHORT
            ).show()
        }
        true
    }
}

private fun distanceMeters(userLatLng: LatLng, loc: LocationData): Double {
    val results = FloatArray(1)
    Location.distanceBetween(
        userLatLng.latitude,
        userLatLng.longitude,
        loc.lat,
        loc.lng,
        results
    )
    return results[0].toDouble()
}

private fun createPinBitmap(colorInt: Int, sizePx: Float): Bitmap {
    val bitmap = Bitmap.createBitmap(sizePx.toInt(), sizePx.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = colorInt }
    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = sizePx * 0.12f
    }
    val radius = sizePx * 0.45f
    val center = sizePx / 2f
    canvas.drawCircle(center, center, radius, paint)
    canvas.drawCircle(center, center, radius, strokePaint)
    return bitmap
}

private fun createEmojiBitmap(emoji: String, sizePx: Float): Bitmap {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = sizePx
        textAlign = Paint.Align.LEFT
    }
    val baseline = -paint.ascent()
    val width = (paint.measureText(emoji) + 0.5f).toInt()
    val height = (baseline + paint.descent() + 0.5f).toInt()
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawText(emoji, 0f, baseline, paint)
    return bitmap
}
