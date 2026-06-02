import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.concurrent.CopyOnWriteArrayList

data class RemoteLocation(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val description: String = "",
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

data class CreateMapRequest(
    val name: String,
    val description: String,
    val locations: List<RemoteLocation>
)

val dynamicMaps = CopyOnWriteArrayList<RemoteMap>()
val dynamicLocations = mutableMapOf<String, List<RemoteLocation>>()

fun main() {
    val initialMaps = listOf(
        RemoteMap("old-town-server", "Old Town", "Traverse the golden path of Krakow's Old Town.", 4, 50.0619, 19.9368),
        RemoteMap("kazimierz-server", "Kazimierz", "Explore the secrets of the Jewish District.", 3, 50.0519, 19.9461),
        RemoteMap("wawel-server", "Wawel", "Enter the legendary hill of kings.", 3, 50.0544, 19.9356)
    )
    dynamicMaps.addAll(initialMaps)

    dynamicLocations["old-town-server"] = listOf(
        RemoteLocation("rynek", "Main Square", 50.0619, 19.9368, "The Main Square was the largest medieval town square in Europe, designed in 1257. It remains the vibrant heart of Krakow, hosting markets and historic events for centuries.", true),
        RemoteLocation("mariacki", "St. Mary's Basilica", 50.0617, 19.9395, "A stunning Brick Gothic church famous for its wooden altarpiece carved by Veit Stoss. Every hour, a trumpet signal sounds from the taller tower to commemorate a legendary bugler.", true),
        RemoteLocation("barbakan", "Barbican", 50.0655, 19.9405, "A powerful fortified outpost once connected to the city walls, built in 1498 to protect the Floriańska Gate. It is one of the few remaining examples of its type in Europe."),
        RemoteLocation("florianska", "Floriańska Gate", 50.0641, 19.9403, "The main entrance to the Old Town and the starting point of the Royal Route. It survived the 19th-century demolition of city walls thanks to local preservation efforts.")
    )

    dynamicLocations["kazimierz-server"] = listOf(
        RemoteLocation("szeroka", "Szeroka Street", 50.0516, 19.9476, "Once the center of the Jewish city of Kazimierz, this square is lined with historic synagogues."),
        RemoteLocation("synagoga", "Old Synagogue", 50.0513, 19.9485, "The oldest surviving Jewish house of prayer in Poland."),
        RemoteLocation("plac", "Nowy Square", 50.0519, 19.9461, "The central hub of Kazimierz life, famous for zapiekanki.")
    )

    dynamicLocations["wawel-server"] = listOf(
        RemoteLocation("wawel", "Wawel Castle", 50.0544, 19.9356, "A symbol of Polish national identity where kings lived.", true),
        RemoteLocation("katedra", "Wawel Cathedral", 50.0544, 19.9347, "The site of royal coronations and the final resting place of heroes."),
        RemoteLocation("smok", "Dragon's Den", 50.0537, 19.9346, "A limestone cave at the foot of Wawel Hill, home of the Wawel Dragon.")
    )

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        routing {
            get("/maps") {
                call.respond(dynamicMaps)
            }

            post("/maps") {
                val request = call.receive<CreateMapRequest>()
                val newId = "custom-${System.currentTimeMillis()}"
                val avgLat = if (request.locations.isNotEmpty()) request.locations.map { it.lat }.average() else 50.06
                val avgLng = if (request.locations.isNotEmpty()) request.locations.map { it.lng }.average() else 19.93

                val newMap = RemoteMap(
                    id = newId,
                    name = request.name,
                    description = request.description,
                    dragonsCount = request.locations.size,
                    centerLat = avgLat,
                    centerLng = avgLng
                )

                dynamicMaps.add(newMap)
                dynamicLocations[newId] = request.locations

                call.respond(newMap)
            }

            get("/maps/{mapId}/locations") {
                val mapId = call.parameters["mapId"] ?: ""
                val locations = dynamicLocations[mapId] 
                    ?: dynamicLocations["$mapId-server"] 
                    ?: emptyList()
                call.respond(locations)
            }
        }
    }.start(wait = true)
}
