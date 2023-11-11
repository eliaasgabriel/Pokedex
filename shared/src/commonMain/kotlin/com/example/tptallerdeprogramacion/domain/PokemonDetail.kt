package com.example.tptallerdeprogramacion.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetail(
    @SerialName(value = "sprites")
    val image: PokemonImage
)
