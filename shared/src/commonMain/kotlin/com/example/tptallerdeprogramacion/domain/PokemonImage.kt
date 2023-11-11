package com.example.tptallerdeprogramacion.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonImage (
    @SerialName(value = "front_default")
    val image: String,
    var url : String? = ""
)