package com.example.tptallerdeprogramacion.domain.repositories

import com.example.tptallerdeprogramacion.data.PokedexClient
import com.example.tptallerdeprogramacion.domain.PokedexResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class PokedexRepository (private val pokedexClient: PokedexClient) {

    suspend fun getPokedex(): PokedexResponse {
        return withContext(Dispatchers.IO) {
            pokedexClient.obtenerPokedex()
        }
    }
}