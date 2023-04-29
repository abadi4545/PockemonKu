package com.arkam.pockemonku.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkam.pockemonku.network.createPokemonService
import com.arkam.pockemonku.persistence.AppDatabase
import com.arkam.pockemonku.repository.MainRepository
import java.lang.IllegalArgumentException

/*created the ViewModel and implemented a ViewModelProvider.Factory that gets as a parameter the dependencies needed to create MainActivityViewModel: the context.*/
class MainActivityViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(
                MainRepository(
                    createPokemonService(),
                    AppDatabase.getAppDatabase(context)!!.pokemonDao()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }

}