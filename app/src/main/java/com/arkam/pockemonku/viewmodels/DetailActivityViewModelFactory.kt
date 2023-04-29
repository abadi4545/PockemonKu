package com.arkam.pockemonku.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkam.pockemonku.network.createPokemonService
import com.arkam.pockemonku.repository.DetailRepository
import java.lang.IllegalArgumentException

class DetailActivityViewModelFactory : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the given `Class`.
     *
     *
     *
     * @param modelClass a `Class` whose instance is requested
     * @param <T>        The type parameter for the ViewModel.
     * @return a newly created ViewModel
    </T> */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailActivityViewModel::class.java)) {
            return DetailActivityViewModel(DetailRepository(createPokemonService())) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }

}