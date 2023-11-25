package com.example.tptallerdeprogramacion.android.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tptallerdeprogramacion.android.ui.viewModels.screenStates.PokedexScreenState
import com.example.tptallerdeprogramacion.domain.PokedexDBResults
import com.example.tptallerdeprogramacion.domain.Pokemon
import com.example.tptallerdeprogramacion.domain.repositories.PokedexDBRepository
import com.example.tptallerdeprogramacion.domain.repositories.PokedexRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PokedexViewModel(val pokedexRepository: PokedexRepository) : ViewModel() {


    private val _screenState: MutableStateFlow<PokedexScreenState> = MutableStateFlow(
        PokedexScreenState.Loading
    )
    var screenState: Flow<PokedexScreenState> = _screenState

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("PokedexViewModel", "Error al consultar API: ${throwable.message}")
        }

    fun loadPokemons(dbRepository: PokedexDBRepository) {
        viewModelScope.launch(coroutineExceptionHandler) {

                kotlin.runCatching {

                    pokedexRepository.getPokedex()

                }.onSuccess {
                    if (it.results != null) {

                        _screenState.value = PokedexScreenState.ShowPokedex(it.results!!)

                        screenState = _screenState
                        //Se borran los antiguos registros en la base de datos
                        dbRepository.deletePokemons()

                        //Se ingresan los nuevos registros a la base como backup por si existe un error
                        for (pokemon in it.results!!) {
                            dbRepository.putPokemon(PokedexDBResults(pokemon.name, pokemon.url))
                        }
                    } else {
                        _screenState.value = PokedexScreenState.Error
                    }
                }.onFailure {

                    //Se agarran los datos guardados en la base para mostrarlos otra ves
                    val pokedexResponse = mutableListOf<Pokemon>()

                    for (pokemonData in dbRepository.getPokemons()) {
                        pokedexResponse.add(Pokemon(pokemonData.nameData, pokemonData.urlData))
                    }
                    if (pokedexResponse.isNotEmpty()) {
                        _screenState.value = PokedexScreenState.ShowPokedex(pokedexResponse)

                        screenState = _screenState
                    } else {
                        _screenState.value = PokedexScreenState.Error
                    }

                    Log.d("PokedexViewModel", "Error al consultar API: ${it.message}")
                }
            }

    }


}