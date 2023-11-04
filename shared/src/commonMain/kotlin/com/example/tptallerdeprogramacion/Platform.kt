package com.example.tptallerdeprogramacion

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform