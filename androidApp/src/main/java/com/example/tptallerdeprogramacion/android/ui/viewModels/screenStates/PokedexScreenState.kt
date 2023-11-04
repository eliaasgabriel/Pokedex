package com.example.tptallerdeprogramacion.android.ui.viewModels.screenStates

import com.example.tptallerdeprogramacion.domain.PokedexResponse
import com.example.tptallerdeprogramacion.domain.Pokemon

sealed class PokedexScreenState {
    object Loading : PokedexScreenState()

    object Error : PokedexScreenState()

    class ShowPokedex(val pokedex: List<Pokemon>) : PokedexScreenState()
}