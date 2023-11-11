package com.example.tptallerdeprogramacion

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.pokedex.DatabasePokemon

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()


actual class DatabaseDriverFactory{
    actual fun createDriver() : SqlDriver{
        return NativeSqliteDriver(DatabasePokemon.Schema, "pokedex_db")
    }
}