package com.arkam.pockemonku.view.adapter


import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.arkam.pockemonku.R
import com.arkam.pockemonku.model.Pokemon
import com.arkam.pockemonku.view.ui.main.DetailActivity
import com.arkam.pockemonku.view.ui.main.MainActivity
import kotlinx.android.synthetic.main.item_pokemon_list.view.*

class PokemonListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(pokemonModel: Pokemon) {
        itemView.pokemon_name_text_view.text = pokemonModel.name
        Glide.with(itemView.context)
            .load(pokemonModel.getImageUrl())
            .into(itemView.pokemon_image_view)
    }
}

class PokemonListAdapter(private val mainActivity: MainActivity) :
    RecyclerView.Adapter<PokemonListItemViewHolder>() {
    private var listOfPokemons = listOf<Pokemon>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon_list, parent, false)
        return PokemonListItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listOfPokemons.size
    }

    override fun onBindViewHolder(holder: PokemonListItemViewHolder, position: Int) {
        holder.bindView(listOfPokemons[position])
        holder.itemView.setOnClickListener {
            val options = ActivityOptions
                .makeSceneTransitionAnimation(
                    mainActivity,
                    holder.itemView.findViewById<ImageView>(R.id.pokemon_image_view),
                    "transition_pokemon"
                )

            val intent = Intent(mainActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.ARG_POKEMON_NAME, listOfPokemons[position].name)
            intent.putExtra(
                DetailActivity.ARG_POKEMON_IMAGE_URL,
                listOfPokemons[position].getImageUrl()
            )

            // start the new activity
            mainActivity.startActivity(intent, options.toBundle())
        }
    }


    fun setPokemonList(listOfPokemons: List<Pokemon>) {
        this.listOfPokemons = listOfPokemons
        notifyDataSetChanged()
    }
}