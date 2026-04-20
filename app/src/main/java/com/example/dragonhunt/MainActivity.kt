package com.example.dragonhunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.dragonhunt.ui.theme.DragonHuntTheme
import com.example.dragonhunt.model.MapData
import com.example.dragonhunt.ui.screens.addroute.AddRouteScreen
import com.example.dragonhunt.ui.screens.challenge.ChallengeScreen
import com.example.dragonhunt.ui.screens.collection.CollectionScreen
import com.example.dragonhunt.ui.screens.map.MainMapScreen
import com.example.dragonhunt.ui.screens.mapselection.MapSelectionScreen
import com.example.dragonhunt.ui.screens.splash.SplashScreen
import org.maplibre.android.MapLibre

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MapLibre.getInstance(this)

        setContent {
            DragonHuntTheme {
                var currentScreen by remember { mutableStateOf("splash") }
                var selectedMap by remember { mutableStateOf<MapData?>(null) }

                when (currentScreen) {
                    "splash" -> SplashScreen(
                        onTimeout = {
                            currentScreen = "map_selection"
                        }
                    )

                    "map_selection" -> MapSelectionScreen(
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
                        onRouteCreated = { title, description, dragons ->
                            currentScreen = "map_selection"
                        }
                    )

                    "main_map" -> MainMapScreen(
                        mapId = selectedMap?.id ?: "wawel-server",
                        initialLat = selectedMap?.centerLat ?: 50.0619,
                        initialLng = selectedMap?.centerLng ?: 19.9368,
                        onBack = {
                            currentScreen = "map_selection"
                        },
                        onLocationClick = { _ ->
                            currentScreen = "challenge"
                        },
                        onCollectionClick = {
                            currentScreen = "collection"
                        }
                    )

                    "challenge" -> ChallengeScreen(
                        dragonName = "Mystic Dragon",
                        onBack = {
                            currentScreen = "main_map"
                        },
                        onUnlocked = {
                            currentScreen = "collection"
                        }
                    )

                    "collection" -> CollectionScreen(
                        onBack = {
                            currentScreen = "map_selection"
                        }
                    )
                }
            }
        }
    }
}
