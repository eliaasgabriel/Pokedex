package com.example.tptallerdeprogramacion

import app.cash.sqldelight.db.SqlDriver

interface Platform {
    val name: String
}

expect class DatabaseDriverFactory{
    fun createDriver(): SqlDriver
}
