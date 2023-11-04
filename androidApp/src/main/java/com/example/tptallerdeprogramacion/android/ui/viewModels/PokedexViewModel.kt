package com.example.tptallerdeprogramacion.android.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tptallerdeprogramacion.android.ui.viewModels.screenStates.PokedexScreenState
import com.example.tptallerdeprogramacion.domain.PokedexResponse
import com.example.tptallerdeprogramacion.domain.Pokemon
import com.example.tptallerdeprogramacion.domain.repositories.PokedexRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PokedexViewModel(val pokedexRepository : PokedexRepository) : ViewModel() {

    val pokedex = MutableLiveData<List<Pokemon>>()

    suspend fun obtenerPokedex() : PokedexResponse {
            return pokedexRepository.getPokedex()
    }

    private val _screenState: MutableStateFlow<PokedexScreenState> = MutableStateFlow(
        PokedexScreenState.Loading)
    val screenState: Flow<PokedexScreenState> = _screenState

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("PokedexViewModel", "Error retrieving pokedex: ${throwable.message}")
        }

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            kotlin.runCatching {
                pokedexRepository.getPokedex()
            }.onSuccess {
                if (it.results != null) {
                    //pokedex.postValue(it.results!!)
                    _screenState.value = PokedexScreenState.ShowPokedex(it.results!!)
                } else {
                    _screenState.value = PokedexScreenState.Error
                }
            }.onFailure {
                Log.d("PokedexViewModel", "Error retrieving pokedex: ${it.message}")
                _screenState.value = PokedexScreenState.Error
            }

        }
    }

}