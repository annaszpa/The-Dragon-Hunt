package com.example.dragonhunt.ui.screens.challenge

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChallengeScreen(
    dragonName: String,
    onBack: () -> Unit,
    onUnlocked: () -> Unit
) {
    var shakeCount by remember(dragonName) { mutableIntStateOf(0) }
    val maxShakes = 10
    val progress = (shakeCount.toFloat() / maxShakes.toFloat()).coerceIn(0f, 1f)

    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val accelerometer = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    val shakeDetector = remember(dragonName) {
        ShakeDetector(
            thresholdG = 2.1f,
            cooldownMs = 350L,
            onShake = {
                if (shakeCount < maxShakes) {
                    shakeCount++
                }
            }
        )
    }

    DisposableEffect(accelerometer) {
        if (accelerometer == null) {
            return@DisposableEffect onDispose { }
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                shakeDetector.onSensorChanged(event.values)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "egg_pulse")
    val eggScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF8B0000),
                        Color(0xFF4A0000)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "YOU FOUND AN EGG!!!",
                fontSize = 36.sp,
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (dragonName.isNotBlank()) {
                Text(
                    text = dragonName,
                    fontSize = 18.sp,
                    color = Color(0xFFE5DDD3),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(300.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(eggScale),
                    shape = RoundedCornerShape(100.dp),
                    color = Color(0xFFD4AF37).copy(alpha = 0.2f + (0.2f * progress))
                ) {}

                Text(
                    text = if (shakeCount >= maxShakes) "🐲" else "🥚",
                    fontSize = 120.sp,
                    modifier = Modifier.scale(eggScale)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (shakeCount < maxShakes) {
                        "SHAKE YOUR PHONE"
                    } else {
                        "DRAGON UNLOCKED!"
                    },
                    color = Color(0xFFD4AF37),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(6.dp)),
                    color = Color(0xFFD4AF37),
                    trackColor = Color.White.copy(alpha = 0.1f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (shakeCount >= maxShakes) {
                    Button(
                        onClick = onUnlocked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD4AF37)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "ADD TO COLLECTION",
                            color = Color(0xFF4A0000),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Cancel", color = Color(0xFFE5DDD3))
                }
            }
        }
    }
}
