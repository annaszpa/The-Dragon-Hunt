package com.example.dragonhunt.ui.screens.addroute

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonhunt.model.CustomDragon

@Composable
fun AddRouteScreen(
    onBack: () -> Unit,
    onRouteCreated: (String, String, List<CustomDragon>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val selectedDragons = remember { mutableStateListOf<CustomDragon>() }
    var showDragonPicker by remember { mutableStateOf(false) }
    var pickingLocationForIndex by remember { mutableStateOf<Int?>(null) }

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
                .padding(horizontal = 20.dp)
        ) {
            AddRouteHeader(onBack = onBack)

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    RouteDetailsForm(
                        title = title,
                        onTitleChange = { title = it },
                        description = description,
                        onDescriptionChange = { description = it }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DRAGONS ON THIS ROUTE",
                            color = Color(0xFFD4AF37),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        
                        Button(
                            onClick = { showDragonPicker = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37).copy(alpha = 0.2f)),
                            border = BorderStroke(1.dp, Color(0xFFD4AF37)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("+ ADD DRAGON", color = Color(0xFFD4AF37), fontSize = 12.sp)
                        }
                    }
                }

                if (selectedDragons.isEmpty()) {
                    item {
                        Text(
                            "No dragons added to this route yet.",
                            color = Color(0xFFE5DDD3).copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                itemsIndexed(selectedDragons) { index, dragon ->
                    DragonConfigCard(
                        dragon = dragon,
                        onRemove = { selectedDragons.removeAt(index) },
                        onUpdate = { updated ->
                            selectedDragons[index] = updated
                        },
                        onPickLocation = {
                            pickingLocationForIndex = index
                        }
                    )
                }
            }

            Button(
                onClick = { 
                    onRouteCreated(title, description, selectedDragons.toList()) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp)
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37), contentColor = Color(0xFF4A0000)),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank() && selectedDragons.isNotEmpty()
            ) {
                Text("PROCLAIM NEW ROUTE", fontWeight = FontWeight.ExtraBold)
            }
        }

        if (showDragonPicker) {
            DragonPickerModal(
                onDismiss = { showDragonPicker = false },
                onDragonSelected = { breed ->
                    selectedDragons.add(CustomDragon(breed = breed, isSelected = true))
                    showDragonPicker = false
                }
            )
        }

        pickingLocationForIndex?.let { index ->
            LocationPickerOverlay(
                onConfirm = { lat, lng ->
                    val dragon = selectedDragons[index]
                    selectedDragons[index] = dragon.copy(lat = lat, lng = lng)
                    pickingLocationForIndex = null
                },
                onDismiss = { pickingLocationForIndex = null }
            )
        }
    }
}
