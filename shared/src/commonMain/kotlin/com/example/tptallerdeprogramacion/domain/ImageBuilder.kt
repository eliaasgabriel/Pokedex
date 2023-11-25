package com.example.tptallerdeprogramacion.domain

object ImageBuilder {

    fun buildPokemonImageByUrl(detailUrl: String): String {
        return if (detailUrl.split('/').size > 5) {

            val pokemonId = detailUrl.split('/')[6]
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
        } else {
            ""
        }
    }
}