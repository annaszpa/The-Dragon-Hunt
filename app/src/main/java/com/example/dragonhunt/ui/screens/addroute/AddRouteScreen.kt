package com.example.dragonhunt.ui.screens.addroute

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    val dragonsList = remember { 
        mutableStateListOf<CustomDragon>().apply {
            val names = listOf(
                "Golden King", "Silver Knight", "Ruby Guardian", "Emerald Seeker", 
                "Sapphire Wing", "Ancient Scholar", "Cloth Hall Guard", "Vistula Terror",
                "Wawel Protector", "Market Sage", "Tower Watcher", "Hidden Shadow",
                "Fire Breather", "Ice Frost", "Bronze Titan", "Iron Claw",
                "Pearl Spirit", "Obsidian Drake", "Amber Eye", "Royal Sentinel"
            )
            names.forEach { add(CustomDragon(it)) }
        }
    }

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
                    Text(
                        text = "SELECT DRAGONS & SET LOCATIONS",
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                items(dragonsList) { dragon ->
                    DragonSelectionCard(
                        dragon = dragon,
                        onToggle = { isSelected ->
                            val index = dragonsList.indexOf(dragon)
                            if (index != -1) {
                                dragonsList[index] = dragonsList[index].copy(isSelected = isSelected)
                            }
                        },
                        onLatChange = { lat ->
                            val index = dragonsList.indexOf(dragon)
                            if (index != -1) {
                                dragonsList[index] = dragonsList[index].copy(lat = lat)
                            }
                        },
                        onLngChange = { lng ->
                            val index = dragonsList.indexOf(dragon)
                            if (index != -1) {
                                dragonsList[index] = dragonsList[index].copy(lng = lng)
                            }
                        }
                    )
                }
            }

            Button(
                onClick = { 
                    val selected = dragonsList.filter { it.isSelected }
                    onRouteCreated(title, description, selected) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp)
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37), contentColor = Color(0xFF4A0000)),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank() && dragonsList.any { it.isSelected }
            ) {
                Text("PROCLAIM NEW ROUTE", fontWeight = FontWeight.ExtraBold)
            }

        }
    }
}
