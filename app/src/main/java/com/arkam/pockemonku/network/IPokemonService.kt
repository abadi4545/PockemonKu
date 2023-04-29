package com.arkam.pockemonku.network

import com.arkam.pockemonku.model.PokemonInfo
import com.arkam.pockemonku.model.PokemonResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

fun createPokemonService(): IPokemonService {
//use OkHttpClient to create Retrofit instance
    val okHttpClient = OkHttpClient
        .Builder()
        .build()

    val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://pokeapi.co/api/v2/") //specify the relative endpoint(Pokemon) URL of REST resource.
        .addConverterFactory(MoshiConverterFactory.create()) //Here we are using moshi because it is way faster than Gson
        .build()

    return retrofit.create(IPokemonService::class.java)
}

//use the Retrofit annotations to create the service interface with required mapping information and request/response classes.
interface IPokemonService {
    @GET("pokemon")
    fun fetchPokemonList(
        @Query("limit") limit: Int = 500,
        @Query("offset") offset: Int = 0
    ): Call<PokemonResponse>  //return the data that is expected from the server wrapped it into a typed Retrofit Call< > class.

    @GET("pokemon/{name}")
    fun fetchPokemonDetails(
        @Path("name")
        name: String
    ): Call<PokemonInfo>
}