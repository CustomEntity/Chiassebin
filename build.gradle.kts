val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val koinVersion: String by project
val koinKtorVersion: String by project
val koinKspVersion: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    kotlin("kapt") version "1.8.10"
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

apply {
    plugin("com.google.devtools.ksp")
}

group = "fr.customentity"
version = "0.0.1"
application {
    mainClass.set("fr.customentity.launcher.LauncherKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}


repositories {
    mavenCentral()
}

dependencies {

    // Koin dependencies
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinKtorVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinKtorVersion")
    ksp("io.insert-koin:koin-ksp-compiler:1.1.1")


    // Scylla dependencies
    kapt("com.scylladb:java-driver-mapper-processor:4.14.1.0")
    implementation("com.scylladb:java-driver-core:4.14.1.0")
    implementation("com.scylladb:java-driver-query-builder:4.14.1.0")
    implementation("com.scylladb:java-driver-mapper-runtime:4.14.1.0")




    // Ktor dependencies
    implementation("io.ktor:ktor-client-core-jvm:2.2.3")
    implementation("io.ktor:ktor-client-apache-jvm:2.2.3")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")

    // Misc dependencies
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}