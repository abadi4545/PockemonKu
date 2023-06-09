package com.arkam.pockemonku.extensions

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arkam.pockemonku.R

fun AppCompatActivity.changeColor(color: Int) {
    window.statusBarColor = color
    supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun String.getTypeColor(): Int {
    return when (this) {
        "fighting" -> R.color.fighting
        "flying" -> R.color.flying
        "poison" -> R.color.poison
        "ground" -> R.color.ground
        "rock" -> R.color.rock
        "bug" -> R.color.bug
        "ghost" -> R.color.ghost
        "steel" -> R.color.steel
        "fire" -> R.color.fire
        "water" -> R.color.water
        "grass" -> R.color.grass
        "electric" -> R.color.electric
        "psychic" -> R.color.psychic
        "ice" -> R.color.ice
        "dragon" -> R.color.dragon
        "fairy" -> R.color.fairy
        "dark" -> R.color.dark
        else -> R.color.gray_21
    }
}