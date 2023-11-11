import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id ("kotlinx-serialization")
    id("app.cash.sqldelight") version "2.0.0"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

    }

    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val ktorVersion = "2.3.5"
        val sqlDeLightVersion = "2.0.0"

        val commonMain by getting {
            dependencies {

                //Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                // General
                implementation ("androidx.core:core-ktx:1.7.0")
                implementation ("androidx.appcompat:appcompat:1.6.0")
                implementation ("com.google.android.material:material:1.8.0")
                implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

                // Plugin de ktor
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                // Librería de logging multiplataforma
                implementation("io.github.aakira:napier:2.6.1")

                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3"){
                    version {
                        // Necesario para evitar que otras dependencias reemplacen coroutines, por ej: Ktor
                        strictly("1.7.3")
                    }
                }

                //SqlDeLight
                implementation ("app.cash.sqldelight:sqlite-driver:$sqlDeLightVersion")

            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting{
            dependencies{
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

                implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")

                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

                //SqlDeLight
                implementation ("app.cash.sqldelight:android-driver:$sqlDeLightVersion")

            }
        }

        val iosMain by getting  {

            dependsOn(commonMain)

            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")

                implementation ("app.cash.sqldelight:native-driver:$sqlDeLightVersion")
            }
        }

    }
}

sqldelight{
    databases{
        create("DatabasePokemon"){
            packageName.set("com.example.pokedex")
        }
    }
}

android {
    namespace = "com.example.tptallerdeprogramacion"
    compileSdk = 33
    defaultConfig {
        minSdk = 28
    }
}