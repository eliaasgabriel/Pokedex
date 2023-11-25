package com.example.tptallerdeprogramacion.domain.repositories

import com.example.pokedex.db.DatabasePokemon
import com.example.tptallerdeprogramacion.DatabaseDriverFactory
import com.example.tptallerdeprogramacion.domain.PokedexDBResults

class PokedexDBRepository(dbDriverFactory: DatabaseDriverFactory) {

    private val database = DatabasePokemon(dbDriverFactory.createDriver())
    private val pokedexQuery = database.pokedexQueries


    private fun mapPokemonSelecting(
        nameDb: String,
        urlDb: String
    ): PokedexDBResults {
        return PokedexDBResults(nameDb, urlDb)
    }

    fun putPokemon(pokemon: PokedexDBResults) {
        pokemon.urlData?.let { pokedexQuery.insertPokemon(pokemon.nameData, it) }
    }

    fun getPokemons(): List<PokedexDBResults> {
        return pokedexQuery.obtenerPokemons(::mapPokemonSelecting).executeAsList()
    }

    fun deletePokemons() {
        pokedexQuery.transaction {
            pokedexQuery.borrarPokemons()
        }
    }
}