package com.example.tptallerdeprogramacion.domain.repositories

import com.example.tptallerdeprogramacion.data.PokedexClient
import com.example.tptallerdeprogramacion.domain.PokedexResponse

class PokedexRepository(private val pokedexClient: PokedexClient) {

    suspend fun getPokedex(): PokedexResponse {

        return pokedexClient.obtenerPokedex()

    }
}