package com.example.tptallerdeprogramacion.domain.repositories

import com.example.pokedex.DatabasePokemon
import com.example.tptallerdeprogramacion.DatabaseDriverFactory
import com.example.tptallerdeprogramacion.domain.PokedexDBResults

class PokedexDBRepository(dbDriverFactory: DatabaseDriverFactory) {

    private val database = DatabasePokemon(dbDriverFactory.createDriver())
    private val pokedexQuery = database.pokedexQueries


    private fun mapPokemonSelecting(
        nameDb: String,
        urlDb: String
    ) : PokedexDBResults{
        return PokedexDBResults(nameDb, urlDb)
    }

    fun insertarPokemon(pokemon : PokedexDBResults){
        pokedexQuery.insertPokemon(pokemon.nameData, pokemon.nameData)
    }

    fun obtenerPokemons() : List<PokedexDBResults>{
        return pokedexQuery.obtenerPokemons(::mapPokemonSelecting).executeAsList()
    }

    fun borrarPokemons(){
        pokedexQuery.transaction {
            pokedexQuery.borrarPokemons()
        }
    }
}