import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.gms.google-services")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            //          Koin
            api(libs.koin.core)
            implementation(libs.koin.compose)

            //            Ktor
            implementation(libs.bundles.ktor)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.serialization.kotlinx.json.jvm)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)


            implementation(libs.ktor.client.okhttp)
        }
        jvmMain.dependencies {
            //            DataStore
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)


            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.atech.expensesync.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}
dependencies {
    ksp(libs.room.compiler)
}