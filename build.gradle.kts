plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.0.2").apply(false)
    id("com.android.library").version("8.0.2").apply(false)
    id("com.squareup.sqldelight").version("1.5.4").apply(false)
    kotlin("android").version("1.8.21").apply(false)
    kotlin("multiplatform").version("1.8.21").apply(false)
    id ("org.jetbrains.kotlin.plugin.serialization") version ("1.6.21")
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
