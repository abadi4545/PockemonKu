package com.arkam.pockemonku.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arkam.pockemonku.model.PokemonInfo
import com.arkam.pockemonku.repository.DetailRepository
import com.arkam.pockemonku.viewstate.ViewState

class DetailActivityViewModel(private val detailRepository: DetailRepository) : ViewModel() {

    val pokemonInfoData: LiveData<ViewState<PokemonInfo>> = detailRepository.pokemonDetailsLiveData


    fun fetchPokemonDetails(name: String) {
        detailRepository.getPokemonDetails(name)
    }

}