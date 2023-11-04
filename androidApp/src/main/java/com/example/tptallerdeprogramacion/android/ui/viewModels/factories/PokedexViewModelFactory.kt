package com.example.tptallerdeprogramacion.android.ui.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tptallerdeprogramacion.android.ui.viewModels.PokedexViewModel
import com.example.tptallerdeprogramacion.data.PokedexClient
import com.example.tptallerdeprogramacion.domain.repositories.PokedexRepository

class PokedexViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val pokedexRepository = PokedexRepository(PokedexClient())

        return PokedexViewModel(pokedexRepository) as T
    }
}