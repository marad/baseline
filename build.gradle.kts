plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "gh.marad.revolut"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("io.kweb:kweb-core:1.4.8")

    implementation("io.javalin:javalin:6.1.3")
    implementation("io.github.marad:kotlin-html-dsl:0.1.2")
    implementation("org.slf4j:slf4j-simple:2.0.3")

    implementation("party.iroiro.luajava:luajava:3.5.0")
    implementation("party.iroiro.luajava:lua54:3.5.0")
    runtimeOnly("party.iroiro.luajava:lua54-platform:3.5.0:natives-desktop")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(18)
}

application {
    mainClass.set("gh.marad.baseline.MainKt")
}