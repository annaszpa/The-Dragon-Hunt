package com.example.dragonhunt.ui.screens.challenge

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    var shakeCount by remember { mutableStateOf(0) }
    val maxShakes = 10
    val progress = shakeCount.toFloat() / maxShakes

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
                    colors = listOf(Color(0xFF8B0000), Color(0xFF4A0000))
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
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(300.dp)
            ) {
                Surface(
                    modifier = Modifier.size(200.dp).scale(eggScale),
                    shape = RoundedCornerShape(100.dp),
                    color = Color(0xFFD4AF37).copy(alpha = 0.2f * (1 + progress))
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
                    text = if (shakeCount < maxShakes) "TAP TO SHAKE THE EGG" else "DRAGON UNLOCKED!",
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

                if (shakeCount < maxShakes) {
                    Button(
                        onClick = { 
                            if (shakeCount < maxShakes) shakeCount++ 
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD4AF37),
                            contentColor = Color(0xFF4A0000)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("SHAKE IT!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                } else {
                    Button(
                        onClick = onUnlocked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("ADD TO COLLECTION", color = Color(0xFF4A0000), fontWeight = FontWeight.Bold)
                    }
                }

                TextButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Cancel", color = Color(0xFFE5DDD3))
                }
            }
        }
    }
}
