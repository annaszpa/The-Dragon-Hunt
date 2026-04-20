import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

data class RemoteLocation(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val unlocked: Boolean = false
)

data class RemoteMap(
    val id: String,
    val name: String,
    val description: String,
    val dragonsCount: Int,
    val centerLat: Double,
    val centerLng: Double
)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        routing {
            get("/maps") {
                val maps = listOf(
                    RemoteMap("old-town-server", "Old Town", "Traverse the golden path of Krakow's Old Town.", 4, 50.0619, 19.9368),
                    RemoteMap("kazimierz-server", "Kazimierz", "Explore the secrets of the Jewish District.", 3, 50.0519, 19.9461),
                    RemoteMap("wawel-server", "Wawel", "Enter the legendary hill of kings.", 3, 50.0544, 19.9356)
                )
                call.respond(maps)
            }

                        get("/maps/{mapId}/locations") {
                val mapId = call.parameters["mapId"]
                val locations = when (mapId) {
                    "old-town-server", "old-town" -> listOf(
                        RemoteLocation("rynek", "Main Square", 50.0619, 19.9368, true),
                        RemoteLocation("mariacki", "St. Mary's Basilica", 50.0617, 19.9395, true),
                        RemoteLocation("barbakan", "Barbican", 50.0655, 19.9405),
                        RemoteLocation("florianska", "Floriańska Gate", 50.0641, 19.9403)
                    )
                    "kazimierz-server", "kazimierz" -> listOf(
                        RemoteLocation("szeroka", "Szeroka Street", 50.0516, 19.9476),
                        RemoteLocation("synagoga", "Old Synagogue", 50.0513, 19.9485),
                        RemoteLocation("plac", "Nowy Square", 50.0519, 19.9461)
                    )
                    "wawel-server", "wawel" -> listOf(
                        RemoteLocation("wawel", "Wawel Castle", 50.0544, 19.9356, true),
                        RemoteLocation("katedra", "Wawel Cathedral", 50.0544, 19.9347),
                        RemoteLocation("smok", "Dragon's Den", 50.0537, 19.9346)
                    )
                    else -> emptyList()
                }
                call.respond(locations)
            }
        }
    }.start(wait = true)
}
