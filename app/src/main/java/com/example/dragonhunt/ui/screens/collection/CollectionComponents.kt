package com.example.dragonhunt.ui.screens.collection

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonhunt.model.DragonInfo
import com.example.dragonhunt.model.RouteCollection

@Composable
fun RouteExpandableCard(route: RouteCollection) {
    var expanded by remember { mutableStateOf(false) }
    val unlockedCount = route.dragons.count { it.unlocked }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFAF7F2).copy(alpha = 0.95f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = route.routeName.uppercase(),
                        color = Color(0xFF8B0000),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Dragons found: $unlockedCount/${route.dragons.size}",
                        color = Color(0xFF5D4037),
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = if (expanded) "▲" else "▼",
                    color = Color(0xFFD4AF37),
                    fontSize = 18.sp
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    route.dragons.forEach { dragon ->
                        DragonListItem(dragon)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DragonListItem(dragon: DragonInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (dragon.unlocked) Color(0xFFD4AF37).copy(alpha = 0.1f) else Color.LightGray.copy(alpha = 0.2f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (dragon.unlocked) "🐉" else "🔒",
            fontSize = 24.sp,
            modifier = Modifier.padding(end = 12.dp)
        )
        Column {
            Text(
                text = if (dragon.unlocked) dragon.name else "???",
                fontWeight = FontWeight.Bold,
                color = if (dragon.unlocked) Color(0xFF2C1810) else Color.Gray,
                fontSize = 16.sp
            )
            if (dragon.unlocked) {
                Text(
                    text = dragon.description,
                    color = Color(0xFF5D4037),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
