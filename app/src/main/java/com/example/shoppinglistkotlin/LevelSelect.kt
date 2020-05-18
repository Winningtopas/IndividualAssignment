package com.example.shoppinglistkotlin

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_level_select.*
import kotlinx.android.synthetic.main.content_level_select.*

class LevelSelect : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        initViews()
    }

    private fun initViews() {
        btnLevel1.setOnClickListener { onLevel1Click() }
        btnLevel2.setOnClickListener { onLevel2Click() }
        btnLevel3.setOnClickListener { onLevel3Click() }
    }

    private fun onLevel1Click(){
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun onLevel2Click(){
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun onLevel3Click(){
        startActivity(Intent(this, MainActivity::class.java))
    }

}
