package com.example.shoppinglistkotlin

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_title_screen.*
import kotlinx.android.synthetic.main.content_title_screen.*

class TitleScreen : AppCompatActivity() {

    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_screen)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Rhytm Game"

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        initViews()
        //mp = MediaPlayer.create(this, R.raw.titlemusic)
        //mp.isLooping = true
        //mp.start()
    }

    private fun initViews() {
        btnPlay.setOnClickListener { onPlayClick() }
        btnShop.setOnClickListener { onShopClick() }
        btnHowToPlay.setOnClickListener { onHowToPlayClick() }
    }

    private fun onPlayClick(){
        startActivity(Intent(this, LevelSelect::class.java))
    }

    private fun onShopClick(){

    }

    private fun onHowToPlayClick(){
        startActivity(Intent(this, MainActivity::class.java))
    }

}
