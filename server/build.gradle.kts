plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.5")
    implementation("io.ktor:ktor-server-netty:2.3.5")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.5")
    implementation("io.ktor:ktor-serialization-gson:2.3.5")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

application {
    mainClass.set("DragonHuntServerKt")
}
