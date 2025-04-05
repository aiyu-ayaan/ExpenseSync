plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {

    androidLibrary {
        namespace = "com.atech.kmpfire"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    jvm()



    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
        }

        androidMain.dependencies {
            implementation(libs.androidx.runner)
            implementation(libs.androidx.core)
        }
        jvmMain.dependencies {

        }

    }
}
