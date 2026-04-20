package com.example.dragonhunt.ui.screens.mapselection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dragonhunt.R
import com.example.dragonhunt.model.MapData
import com.example.dragonhunt.network.RemoteMap
import com.example.dragonhunt.network.RetrofitClient

@Composable
fun MapSelectionScreen(
    onMapSelected: (MapData) -> Unit,
    onCollectionClick: () -> Unit,
    onAddRouteClick: () -> Unit
) {
    var maps by remember { mutableStateOf<List<MapData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val remoteMaps = RetrofitClient.instance.getMaps()
            maps = remoteMaps.map { it.toMapData() }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF8B0000), Color(0xFF4A0000))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MapSelectionHeader()

            Box(modifier = Modifier.weight(1f)) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFD4AF37)
                    )
                } else if (maps.isEmpty()) {
                    Text(
                        text = "No royal maps found...",
                        color = Color(0xFFE5DDD3),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(maps) { map ->
                            MapCard(map = map, onClick = { onMapSelected(map) })
                        }
                    }
                }
            }

            MapSelectionFooter(onAddRouteClick, onCollectionClick)
        }
    }
}

@Composable
private fun MapSelectionHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.herb),
            contentDescription = "Herb",
            modifier = Modifier
                .size(110.dp)
                .shadow(12.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "SELECT YOUR MAP",
            fontSize = 30.sp,
            color = Color(0xFFD4AF37),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Text("Begin your royal adventure in Krakow", color = Color(0xFFE5DDD3))
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun MapSelectionFooter(onAddRoute: () -> Unit, onCollection: () -> Unit) {
    OutlinedButton(
        onClick = onAddRoute,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD4AF37)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("ESTABLISH NEW ROUTE", fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(
        onClick = onCollection,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD4AF37),
            contentColor = Color(0xFF4A0000)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("MY DRAGON COLLECTION", fontWeight = FontWeight.ExtraBold)
    }
}

private fun RemoteMap.toMapData() = MapData(
    id = id,
    name = name,
    description = description,
    dragonsCount = dragonsCount,
    centerLat = centerLat,
    centerLng = centerLng
)
