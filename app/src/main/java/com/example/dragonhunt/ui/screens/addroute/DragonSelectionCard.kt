package com.example.dragonhunt.ui.screens.addroute

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonhunt.model.CustomDragon

@Composable
fun DragonSelectionCard(
    dragon: CustomDragon,
    onToggle: (Boolean) -> Unit,
    onLatChange: (String) -> Unit,
    onLngChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .border(
                1.dp, 
                if (dragon.isSelected) Color(0xFFD4AF37) else Color.Transparent, 
                RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (dragon.isSelected) Color(0xFFD4AF37).copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🐲", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = dragon.name,
                    color = if (dragon.isSelected) Color(0xFFD4AF37) else Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = dragon.isSelected,
                    onCheckedChange = onToggle,
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFD4AF37))
                )
            }
            
            if (dragon.isSelected) {
                DragonGpsInputs(
                    lat = dragon.lat,
                    lng = dragon.lng,
                    onLatChange = onLatChange,
                    onLngChange = onLngChange
                )
            }
        }
    }
}

@Composable
private fun DragonGpsInputs(
    lat: String,
    lng: String,
    onLatChange: (String) -> Unit,
    onLngChange: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = lat,
            onValueChange = onLatChange,
            label = { Text("Lat", fontSize = 10.sp) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(unfocusedTextColor = Color.White, focusedTextColor = Color.White)
        )
        OutlinedTextField(
            value = lng,
            onValueChange = onLngChange,
            label = { Text("Lng", fontSize = 10.sp) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(unfocusedTextColor = Color.White, focusedTextColor = Color.White)
        )
        IconButton(onClick = { }) {
            Icon(Icons.Default.LocationOn, contentDescription = "Map Pin", tint = Color(0xFFD4AF37))
        }
    }
}
