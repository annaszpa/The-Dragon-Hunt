package com.example.dragonhunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.dragonhunt.data.ProgressRepository
import com.example.dragonhunt.data.local.DragonHuntDatabase
import com.example.dragonhunt.model.LocationData
import com.example.dragonhunt.ui.theme.DragonHuntTheme
import com.example.dragonhunt.model.MapData
import com.example.dragonhunt.ui.screens.addroute.AddRouteScreen
import com.example.dragonhunt.ui.screens.challenge.ChallengeScreen
import com.example.dragonhunt.ui.screens.collection.CollectionScreen
import com.example.dragonhunt.ui.screens.map.MainMapScreen
import com.example.dragonhunt.ui.screens.mapselection.MapSelectionScreen
import com.example.dragonhunt.ui.screens.splash.SplashScreen
import com.example.dragonhunt.network.CreateMapRequest
import com.example.dragonhunt.network.RemoteLocation
import com.example.dragonhunt.network.RetrofitClient
import kotlinx.coroutines.launch
import org.maplibre.android.MapLibre

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MapLibre.getInstance(this)

        setContent {
            DragonHuntTheme {
                val context = LocalContext.current
                val database = remember { DragonHuntDatabase.getInstance(context) }
                val progressRepository = remember { ProgressRepository(database.progressDao()) }
                val scope = rememberCoroutineScope()

                var currentScreen by remember { mutableStateOf("splash") }
                var selectedMap by remember { mutableStateOf<MapData?>(null) }
                var selectedLocation by remember { mutableStateOf<LocationData?>(null) }

                when (currentScreen) {
                    "splash" -> SplashScreen(
                        onTimeout = {
                            currentScreen = "map_selection"
                        }
                    )

                    "map_selection" -> MapSelectionScreen(
                        progressRepository = progressRepository,
                        onMapSelected = { map ->
                            selectedMap = map
                            currentScreen = "main_map"
                        },
                        onCollectionClick = {
                            currentScreen = "collection"
                        },
                        onAddRouteClick = {
                            currentScreen = "add_route"
                        }
                    )


                    "add_route" -> AddRouteScreen(
                        onBack = {
                            currentScreen = "map_selection"
                        },
                        onRouteCreated = { title, desc, dragons ->
                            scope.launch {
                                try {
                                    val locations = dragons.mapIndexed { index, cd ->
                                        RemoteLocation(
                                            id = "custom-loc-$index-${System.currentTimeMillis()}",
                                            name = cd.name,
                                            lat = cd.lat.toDoubleOrNull() ?: 50.06,
                                            lng = cd.lng.toDoubleOrNull() ?: 19.93,
                                            description = cd.description,
                                            unlocked = false
                                        )
                                    }
                                    RetrofitClient.instance.createMap(
                                        CreateMapRequest(title, desc, locations)
                                    )
                                    currentScreen = "map_selection"
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    )

                    "main_map" -> MainMapScreen(
                        mapId = selectedMap?.id ?: "wawel-server",
                        progressRepository = progressRepository,
                        onBack = {
                            currentScreen = "map_selection"
                        },
                        onLocationClick = { location ->
                            selectedLocation = location
                            currentScreen = "challenge"
                        },
                        onCollectionClick = {
                            currentScreen = "collection"
                        }
                    )

                    "challenge" -> ChallengeScreen(
                        location = selectedLocation,
                        onBack = {
                            currentScreen = "main_map"
                        },
                        onUnlocked = { location ->
                            val mapId = selectedMap?.id ?: return@ChallengeScreen
                            val mapName = selectedMap?.name ?: mapId
                            scope.launch {
                                progressRepository.unlockLocation(mapId, mapName, location)
                            }
                            currentScreen = "collection"
                        }
                    )

                    "collection" -> CollectionScreen(
                        onBack = {
                            currentScreen = "map_selection"
                        },
                        progressRepository = progressRepository
                    )
                }
            }
        }
    }
}
