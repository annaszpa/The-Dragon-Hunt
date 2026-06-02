package com.example.dragonhunt.ui.screens.addroute

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.dragonhunt.model.CustomDragon
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import java.util.Locale

@Composable
fun RouteDetailsForm(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF7F2).copy(alpha = 0.9f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Route Proclamation Title", color = Color(0xFF8B0000)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD4AF37),
                    unfocusedBorderColor = Color(0xFFD4AF37).copy(alpha = 0.5f),
                    focusedLabelColor = Color(0xFF8B0000),
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Historical Story", color = Color(0xFF8B0000)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                textStyle = TextStyle(color = Color.Black),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD4AF37),
                    unfocusedBorderColor = Color(0xFFD4AF37).copy(alpha = 0.5f),
                    focusedLabelColor = Color(0xFF8B0000),
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
        }
    }
}

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
fun DragonConfigCard(
    dragon: CustomDragon,
    onRemove: () -> Unit,
    onUpdate: (CustomDragon) -> Unit,
    onPickLocation: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF7F2).copy(alpha = 0.95f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🐉 BREED: ${dragon.breed.uppercase()}",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4A0000),
                    fontSize = 12.sp
                )
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Text("✕", color = Color(0xFF8B0000), fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = dragon.name,
                onValueChange = { onUpdate(dragon.copy(name = it)) },
                label = { Text("Given Name (e.g. Barnaba)", fontSize = 10.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD4AF37),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color(0xFF8B0000),
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(), 
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = dragon.lat,
                    onValueChange = { onUpdate(dragon.copy(lat = it)) },
                    label = { Text("Lat", fontSize = 10.sp, color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Medium),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD4AF37),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color(0xFF8B0000),
                        unfocusedLabelColor = Color.Gray
                    )
                )
                OutlinedTextField(
                    value = dragon.lng,
                    onValueChange = { onUpdate(dragon.copy(lng = it)) },
                    label = { Text("Lng", fontSize = 10.sp, color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Medium),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD4AF37),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color(0xFF8B0000),
                        unfocusedLabelColor = Color.Gray
                    )
                )
                
                IconButton(
                    onClick = onPickLocation,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFD4AF37).copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        Icons.Default.LocationOn, 
                        contentDescription = "Pick on map",
                        tint = Color(0xFF8B0000)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = dragon.description,
                onValueChange = { onUpdate(dragon.copy(description = it)) },
                label = { Text("Educational Fact (Trivia)", fontSize = 10.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                textStyle = TextStyle(color = Color.Black),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD4AF37),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color(0xFF8B0000),
                    unfocusedLabelColor = Color.Gray
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DragonPickerModal(
    onDismiss: () -> Unit,
    onDragonSelected: (String) -> Unit
) {
    val names = listOf(
        "Golden King", "Silver Knight", "Ruby Guardian", "Emerald Seeker", 
        "Sapphire Wing", "Ancient Scholar", "Cloth Hall Guard", "Vistula Terror",
        "Wawel Protector", "Market Sage", "Tower Watcher", "Hidden Shadow"
    )

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF7F2))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "CHOOSE DRAGON BREED",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8B0000),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Box(modifier = Modifier.fillMaxWidth().weight(1f, fill = false)) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        names.forEach { name ->
                            TextButton(
                                onClick = { onDragonSelected(name) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Text("🐉", modifier = Modifier.padding(end = 8.dp))
                                    Text(name, color = Color.Black)
                                }
                            }
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationPickerOverlay(
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedLat by remember { mutableStateOf("50.0614") }
    var selectedLng by remember { mutableStateOf("19.9372") }
    
    val context = androidx.compose.ui.platform.LocalContext.current
    val mapView = remember { MapView(context) }

    DisposableEffect(Unit) {
        mapView.onCreate(null)
        mapView.onStart()
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxSize(0.95f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF7F2))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "CHOOSE DRAGON LAIR",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8B0000),
                    fontSize = 20.sp
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = {
                            mapView.apply {
                                getMapAsync { map ->
                                    map.setStyle(Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")) {
                                        map.cameraPosition = CameraPosition.Builder()
                                            .target(LatLng(selectedLat.toDouble(), selectedLng.toDouble()))
                                            .zoom(13.0)
                                            .build()
                                    }
                                    map.addOnMapClickListener { point ->
                                        selectedLat = String.format(Locale.US, "%.6f", point.latitude)
                                        selectedLng = String.format(Locale.US, "%.6f", point.longitude)
                                        true
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "Selected: $selectedLat, $selectedLng",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(32.dp).align(Alignment.Center).offset(y = (-16).dp)
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF8B0000)),
                        border = BorderStroke(1.dp, Color(0xFF8B0000).copy(alpha = 0.5f))
                    ) {
                        Text("CANCEL")
                    }
                    Button(
                        onClick = { onConfirm(selectedLat, selectedLng) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                    ) {
                        Text("CONFIRM", color = Color(0xFF4A0000))
                    }
                }
            }
        }
    }
}
