package com.example.dragonhunt.ui.screens.challenge

import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    private val thresholdG: Float = 2.1f,
    private val cooldownMs: Long = 250L,
    private val timeProvider: () -> Long = { System.currentTimeMillis() },
    private val onShake: () -> Unit
) {
    private var lastShakeTime = 0L

    fun onSensorChanged(values: FloatArray) {
        if (values.size < 3) return

        val gX = values[0] / SensorManager.GRAVITY_EARTH
        val gY = values[1] / SensorManager.GRAVITY_EARTH
        val gZ = values[2] / SensorManager.GRAVITY_EARTH
        val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)

        val now = timeProvider()
        if (gForce > thresholdG && now - lastShakeTime > cooldownMs) {
            lastShakeTime = now
            onShake()
        }
    }
}

