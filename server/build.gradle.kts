plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
}

group = "com.atech.expensesync"
version = "1.0.0"
application {
    mainClass.set("com.atech.expensesync.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.caching.headers.jvm)
    implementation(libs.ktor.serialization.kotlinx.json.jvm)
    implementation(libs.ktor.web.sockets)
    implementation(libs.ktor.server.call.logging.jvm)
    implementation(libs.ktor.server.content.negotiation)

    implementation(libs.firebase.admin)
    implementation (libs.perfmark.api)
    implementation (libs.perfmark.impl)
}