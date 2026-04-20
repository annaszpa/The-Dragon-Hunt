package com.example.dragonhunt.ui.screens.addroute

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddRouteHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .background(Color(0xFFD4AF37), CircleShape)
                .size(40.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF4A0000))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "ESTABLISH ROUTE",
            fontSize = 24.sp,
            color = Color(0xFFD4AF37),
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun RouteDetailsForm(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF7F2).copy(alpha = 0.95f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ROYAL DECREE DETAILS", color = Color(0xFF8B0000), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Route Title") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD4AF37),
                    focusedLabelColor = Color(0xFF8B0000),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8B0000)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Royal Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD4AF37),
                    focusedLabelColor = Color(0xFF8B0000),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8B0000)
                )
            )
        }
    }
}
