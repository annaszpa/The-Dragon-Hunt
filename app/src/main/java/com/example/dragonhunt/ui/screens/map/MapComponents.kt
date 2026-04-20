package com.example.dragonhunt.ui.screens.map

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun MapOverlayButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(16.dp)
            .zIndex(2f)
            .border(1.5.dp, Color(0xFFB8860B), RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD4AF37),
            contentColor = Color(0xFF4A0000)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}
