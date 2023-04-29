package com.arkam.pockemonku.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.arkam.pockemonku.R
import kotlinx.android.synthetic.main.fragment_pokemon_popup.view.*

class PokemonPopupFragmentFailed : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View =
            inflater.inflate(R.layout.fragment_pokemon_popup_failed, container, false)
        rootView.btn_receive.setOnClickListener {
            dismiss()
        }
        return rootView
    }
}