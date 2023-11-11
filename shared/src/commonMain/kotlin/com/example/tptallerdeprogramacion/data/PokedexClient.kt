package com.example.tptallerdeprogramacion.data

import com.example.tptallerdeprogramacion.domain.PokedexResponse
import com.example.tptallerdeprogramacion.domain.Pokemon
import com.example.tptallerdeprogramacion.domain.PokemonDetail
import com.example.tptallerdeprogramacion.domain.PokemonImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class PokedexClient {

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                }
            )
        }
    }

    suspend fun obtenerPokedex(): PokedexResponse {
        val pokedexResponse =
            httpClient.get("https://pokeapi.co/api/v2/pokemon/")
        {
            parameter("limit", "15")
        }.body<PokedexResponse>()
        return pokedexResponse
    }

    suspend fun obtenerImagePokemon(pokeUrl : String): PokemonImage {
        val pokeDetailResponse = httpClient.get(pokeUrl).body<PokemonDetail>()
        pokeDetailResponse.image.url = pokeUrl
        return pokeDetailResponse.image
    }

}