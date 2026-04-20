package com.example.dragonhunt.ui.screens.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonhunt.model.DragonInfo
import com.example.dragonhunt.model.RouteCollection

@Composable
fun CollectionScreen(
    onBack: () -> Unit
) {
    val collections = rememberSampleCollections()

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
                .padding(horizontal = 24.dp)
        ) {
            CollectionHeader(onBack = onBack)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(collections) { route ->
                    RouteExpandableCard(route)
                }
            }
        }
    }
}

@Composable
private fun CollectionHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = onBack,
            modifier = Modifier.size(44.dp).shadow(4.dp, CircleShape),
            color = Color(0xFFD4AF37),
            shape = CircleShape,
            border = BorderStroke(2.dp, Color(0xFFFAF7F2))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF4A0000),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "MY COLLECTION",
            fontSize = 26.sp,
            color = Color(0xFFD4AF37),
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 2.sp
        )
    }
}

@Composable
private fun rememberSampleCollections() = listOf(
    RouteCollection(
        routeName = "Old Town",
        dragons = listOf(
            DragonInfo("Golden King", "Guardian of the Main Square", true),
            DragonInfo("Cloth Hall Keeper", "Lives under the Sukiennice", true),
            DragonInfo("St. Mary's Sentinel", "Watches from the towers", false)
        )
    ),
    RouteCollection(
        routeName = "Wawel Hill",
        dragons = listOf(
            DragonInfo("The Great Wawel Dragon", "The legendary fire-breather", true),
            DragonInfo("Cave Lurker", "Hidden deep in the dragon's den", false)
        )
    ),
    RouteCollection(
        routeName = "Kazimierz",
        dragons = listOf(
            DragonInfo("Jewish Quarter Sage", "Wise protector of the streets", true)
        )
    )
)