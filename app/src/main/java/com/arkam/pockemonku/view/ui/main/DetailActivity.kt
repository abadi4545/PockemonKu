package com.arkam.pockemonku.view.ui.main

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.arkam.pockemonku.Fragments.PokemonPopupFragment
import com.arkam.pockemonku.Fragments.PokemonPopupFragmentFailed
import com.arkam.pockemonku.R
import com.arkam.pockemonku.extensions.getTypeColor
import com.arkam.pockemonku.extensions.hide
import com.arkam.pockemonku.extensions.show
import com.arkam.pockemonku.model.PokemonInfo
import com.arkam.pockemonku.viewmodels.DetailActivityViewModel
import com.arkam.pockemonku.viewmodels.DetailActivityViewModelFactory
import com.arkam.pockemonku.viewstate.Error
import com.arkam.pockemonku.viewstate.Loading
import com.arkam.pockemonku.viewstate.Success
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

class DetailActivity : AppCompatActivity(), SensorEventListener {
    private val viewmodelFactory by lazy { DetailActivityViewModelFactory() }
    private val viewModel: DetailActivityViewModel by viewModels {
        viewmodelFactory
    }
    private var numberOfBalls: Int = 0
    private lateinit var sensorManager: SensorManager
    private lateinit var image: ImageView

    companion object {
        const val ARG_POKEMON_NAME = "pokemon_name"
        const val ARG_POKEMON_IMAGE_URL = "pokemon_image_url"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        numberOfBalls = sharedPreference.getInt("value", 0)
        tv_numberOfBalls.text = numberOfBalls.toString()

        supportActionBar?.elevation = 0f

        // Keeps phone in light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        image = pokemon_image


        val intent: Intent = intent
        val nameFromMainActivity = intent.getStringExtra(ARG_POKEMON_NAME) ?: "pikachu"
        val imageUrl = intent.getStringExtra(ARG_POKEMON_IMAGE_URL)


        Glide.with(this)
            .load(imageUrl)
            .into(pokemon_image)
        pokemon_name.text = nameFromMainActivity

        viewModel.pokemonInfoData.observe(this, { viewState ->
            when (viewState) {
                is Success -> {
                    detail_progress_bar.hide()
                    val pokemonInfo = viewState.data
                    if (pokemonInfo.types.size == 1) {
                        type_name_one.show()
                        type_name_one.text = pokemonInfo.types[0].type.name
                        type_name_one.setBackgroundColor(getColor(pokemonInfo.types[0].type.name.getTypeColor()))

                        type_name_two.hide()
                    } else {
                        type_name_one.show()
                        type_name_one.text = pokemonInfo.types[0].type.name
                        type_name_one.setBackgroundColor(getColor(pokemonInfo.types[0].type.name.getTypeColor()))

                        type_name_two.show()
                        type_name_two.text = pokemonInfo.types[1].type.name
                        type_name_two.setBackgroundColor(getColor(pokemonInfo.types[1].type.name.getTypeColor()))
                    }
                    height.text = pokemonInfo.getHeightString()
                    weight.text = pokemonInfo.getWeightString()
                    tv_id.text = pokemonInfo.getIdString()


                    progress_hp.max = PokemonInfo.maxHp.toFloat()
                    progress_hp.progress = pokemonInfo.getHp().toFloat()


                    progress_attack.max = PokemonInfo.maxAttack.toFloat()
                    progress_attack.progress = pokemonInfo.getAttack().toFloat()


                    progress_defense.max = PokemonInfo.maxDefense.toFloat()
                    progress_defense.progress = pokemonInfo.getDefense().toFloat()


                    progress_speed.max = PokemonInfo.maxSpeed.toFloat()
                    progress_speed.progress = pokemonInfo.getSpeed().toFloat()


                    progress_exp.max = PokemonInfo.maxExp.toFloat()
                    progress_exp.progress = pokemonInfo.getExp().toFloat()
                }
                is Error -> {
                    detail_progress_bar.hide()
                    Toast.makeText(this, viewState.errMsg, Toast.LENGTH_SHORT).show()
                }
                is Loading -> {
                    detail_progress_bar.show()
                }
            }
        })

        viewModel.fetchPokemonDetails(nameFromMainActivity)

        iv_catch.setOnClickListener {
            if (numberOfBalls > 0) {
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_item))
                successFailed()
                numberOfBalls--
                tv_numberOfBalls.text = numberOfBalls.toString()
                saveData(numberOfBalls)
            } else {
                Toast.makeText(this, "Please get more poke balls", Toast.LENGTH_SHORT).show()
            }
        }

        pokemon_image.setOnClickListener {
            Log.d("image", "clicked")
            setUpSensorStuff()
        }
    }

    private fun saveData(value: Int) {
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putInt("value", value)
        editor.apply()
    }

    private fun successFailed() {
        val random: Int = Random().nextInt(4)
        if (random == 1) {
            val dialog = PokemonPopupFragment()
            dialog.show(supportFragmentManager, "got pokemon")
        } else {
            val dialogFailed = PokemonPopupFragmentFailed()
            dialogFailed.show(supportFragmentManager, "failed pokemon")
        }
    }

    private fun setUpSensorStuff() {
        // Create the sensor manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Specify the sensor you want to listen to
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Checks for the sensor we have registered
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            //Log.d("Main", "onSensorChanged: sides ${event.values[0]} front/back ${event.values[1]} ")

            // Sides = Tilting phone left(10) and right(-10)
            val sides = event.values[0]

            // Up/Down = Tilting phone up(10), flat (0), upside-down(-10)
            val upDown = event.values[1]

            image.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }
}