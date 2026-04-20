package com.example.dragonhunt.ui.screens.mapselection

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonhunt.model.MapData

@Composable
fun MapCard(
    map: MapData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .border(3.dp, Color(0xFFD4AF37), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFAF7F2).copy(alpha = 0.95f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = map.name,
                    fontSize = 22.sp,
                    color = Color(0xFF2C1810),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = map.description,
                    fontSize = 14.sp,
                    color = Color(0xFF5D4037),
                    maxLines = 2,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🐉 ", fontSize = 14.sp)
                    Text(
                        text = "${map.dragonsCount} DRAGONS TO FIND",
                        fontSize = 12.sp,
                        color = Color(0xFF8B0000),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Text(
                text = "›", 
                fontSize = 36.sp,
                color = Color(0xFFD4AF37), 
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
