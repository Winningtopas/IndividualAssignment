package com.example.shoppinglistkotlin

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.history_fab -> {
                val profileActivityIntent = Intent(this, GameHistory::class.java)
                startActivity(profileActivityIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
