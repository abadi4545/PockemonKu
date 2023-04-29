package com.arkam.pockemonku.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arkam.pockemonku.model.Pokemon

//Specify SQL queries and associating them with method calls
@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPokemons(pokemons: List<Pokemon>)

    @Update
    fun updatePokemon(pokemon: Pokemon)

    @Delete
    fun deletePokemon(pokemon: Pokemon)

    @Query("SELECT * FROM Pokemon WHERE name == :name")
    fun getPokemonByName(name: String): LiveData<List<Pokemon>>

    @Query("SELECT * FROM Pokemon")
    fun getPokemons(): List<Pokemon>

}