package com.arkam.pockemonku.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arkam.pockemonku.model.Pokemon
import com.arkam.pockemonku.repository.MainRepository
import com.arkam.pockemonku.viewstate.ViewState

// Using LiveData and caching what allWords returns has several benefits:
// - We can put an observer on the data (instead of polling for changes) and only update the
//   the UI when the data actually changes.
// - Repository is completely separated from the UI through the ViewModel.
//created a class called MainActivityViewModel that gets the MainRepository as a parameter and extends ViewModel. The Repository is the only dependency that the ViewModel needs. If other classes would have been needed, they would have been passed in the constructor as well.
class MainActivityViewModel(mainRepository: MainRepository) : ViewModel() {
    //added a public LiveData member variable to cache the list of pokemons.
    val pokemonLiveData: LiveData<ViewState<List<Pokemon>>> = mainRepository.pokemonListLiveData

    init {
        mainRepository.getPokemonList()
    }
}