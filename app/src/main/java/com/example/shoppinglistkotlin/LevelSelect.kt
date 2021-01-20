package com.example.shoppinglistkotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
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

        println();

    }

    private fun initViews() {
        btnLevel1.setOnClickListener { onLevel1Click() }
        btnLevel2.setOnClickListener { onLevel2Click() }
        btnLevel3.setOnClickListener { onLevel3Click() }
    }

    private fun onLevel1Click(){
/*        finish()
        startActivity(Intent(this, MainActivity::class.java))*/

        finish()
        val result = Intent(this, MainActivity::class.java)
        result.putExtra("level", 1)
        startActivity(result)
    }

    private fun onLevel2Click(){
//        finish()
        //startActivityForResult(Intent, int)
        finish()
        val result = Intent(this, MainActivity::class.java)
        result.putExtra("level", 2)
        startActivity(result)
    }

    private fun onLevel3Click(){
        //(activity as MainActivity).stop()
        finish()
        val result = Intent(this, MainActivity::class.java)
        result.putExtra("level", 3)
        startActivity(result)
    }

}
