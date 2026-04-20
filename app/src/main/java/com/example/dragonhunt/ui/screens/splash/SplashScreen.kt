package com.example.dragonhunt.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonhunt.R
import com.example.dragonhunt.ui.theme.DarkRed
import com.example.dragonhunt.ui.theme.Gold
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -180f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "rotation"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(DarkRed),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier
                    .size(260.dp, 300.dp)
                    .scale(scale)
                    .rotate(rotation)
                    .border(8.dp, Gold, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.2f),
                shadowElevation = 20.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    ShimmerEffect()
                    Image(
                        painter = painterResource(id = R.drawable.dragon),
                        contentDescription = "Dragon",
                        modifier = Modifier.size(180.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "The Dragon Hunt",
                fontSize = 36.sp,
                color = Gold,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "loading...",
                fontSize = 16.sp,
                color = Gold.copy(alpha = 0.7f),
                fontFamily = FontFamily.Serif
            )
        }
    }
}

@Composable
private fun ShimmerEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val offset by infiniteTransition.animateFloat(
        initialValue = -500f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "offset"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color.Transparent,
            Gold.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.5f),
            Gold.copy(alpha = 0.3f),
            Color.Transparent
        ),
        start = Offset(offset, offset),
        end = Offset(offset + 200f, offset + 200f)
    )

    Box(modifier = Modifier.fillMaxSize().background(brush))
}
