package com.example.tptallerdeprogramacion.domain.repositories

import com.example.tptallerdeprogramacion.data.PokedexClient
import com.example.tptallerdeprogramacion.domain.PokedexResponse
import com.example.tptallerdeprogramacion.domain.Pokemon
import com.example.tptallerdeprogramacion.domain.PokemonImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class PokedexRepository (private val pokedexClient: PokedexClient) {

    suspend fun getPokedex(): PokedexResponse {

        return pokedexClient.obtenerPokedex()

    }

    suspend fun getImagesPokemon(pokemons : List<Pokemon>): MutableList<PokemonImage> {

        var pokeImagesUrl = mutableListOf<PokemonImage>()

        for (pokemon in pokemons){
            var pokeImageUrl = pokemon.url?.let { pokedexClient.obtenerImagePokemon(it) }

            if (pokeImageUrl != null) {
                pokeImagesUrl.add(pokeImageUrl)

            }

        }

        return pokeImagesUrl
    }
}