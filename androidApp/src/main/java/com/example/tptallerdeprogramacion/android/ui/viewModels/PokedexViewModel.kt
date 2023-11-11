package com.example.tptallerdeprogramacion.android.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tptallerdeprogramacion.android.ui.viewModels.screenStates.PokedexScreenState
import com.example.tptallerdeprogramacion.domain.PokedexDBResults
import com.example.tptallerdeprogramacion.domain.PokedexResponse
import com.example.tptallerdeprogramacion.domain.Pokemon
import com.example.tptallerdeprogramacion.domain.PokemonImage
import com.example.tptallerdeprogramacion.domain.repositories.PokedexDBRepository
import com.example.tptallerdeprogramacion.domain.repositories.PokedexRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PokedexViewModel(val pokedexRepository : PokedexRepository) : ViewModel() {

    val pokedex = MutableLiveData<PokedexResponse>()

    var pokeUrlImages = MutableLiveData<List<PokemonImage>>()

    private val _screenState: MutableStateFlow<PokedexScreenState> = MutableStateFlow(
        PokedexScreenState.Loading)
    val screenState: Flow<PokedexScreenState> = _screenState

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("PokedexViewModel", "Error retrieving pokedex: ${throwable.message}")
        }

    fun cargarPokemons(dbRepository: PokedexDBRepository){
        viewModelScope.launch(coroutineExceptionHandler) {
            kotlin.runCatching {
                pokedexRepository.getPokedex()
            }.onSuccess {
                if (it.results != null) {
                    pokedex.postValue(it!!)
                    _screenState.value = PokedexScreenState.ShowPokedex(it.results!!)

                    //Se borran los antiguos registros en la base de datos
                    dbRepository.borrarPokemons()

                    //Se ingresan los nuevos registros a la base como backup por si existe un error
                    for(pokemon in it.results!!){
                        dbRepository.insertarPokemon(PokedexDBResults(pokemon.name, pokemon.url))
                    }
                } else {
                    _screenState.value = PokedexScreenState.Error
                }
            }.onFailure {
                //Se agarran los datos guarados en la base para mostrarlos otra ves
                var pokedexResponse = mutableListOf<Pokemon>()
                for(pokemonData in dbRepository.obtenerPokemons()){
                    pokedexResponse.add(Pokemon(pokemonData.nameData, pokemonData.urlData))
                }
                pokedex.value?.results = pokedexResponse
                Log.d("PokedexViewModel", "Error retrieving pokedex: ${it.message}")
                _screenState.value = PokedexScreenState.Error
            }

        }
    }

    fun cargarImagenes(pokemons : List<Pokemon>, dbRepository: PokedexDBRepository){
        viewModelScope.launch(){
            kotlin.runCatching {
                pokedexRepository.getImagesPokemon(pokemons)
            }.onSuccess {
                if (it != null) {
                    pokeUrlImages.value = it
                } else {
                }
            }.onFailure {
                Log.d("PokedexViewModel", "Error retrieving pokedex: ${it.message}")
                _screenState.value = PokedexScreenState.Error
            }
        }
    }


}