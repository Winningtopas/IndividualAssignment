package com.example.shoppinglistkotlin

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var gameHistoryRepository: GameHistoryRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var computerOptions: Array<Int> =
        arrayOf(0,1,2,3,4,5)
    private val computerCombination: ArrayList<Int> = arrayListOf()//computerOptions[randomStart]

    private val playerCombination: ArrayList<Int> = arrayListOf()

    private var numberOfPlayerInputs: Int = 0
    private var cantInput: Boolean = true

    private var speed: Float = 50f
    private var wrongAnswers: Int = 0
    private var correctAnswers: Int = 0

    var points: Int = 0
    var starCount: Int = 0

    private lateinit var drumSound: MediaPlayer
    private lateinit var drumSound2: MediaPlayer
    private lateinit var drumSmallSound: MediaPlayer
    private lateinit var drumSmallSound2: MediaPlayer
    private lateinit var tssSound: MediaPlayer
    private lateinit var tssSound2: MediaPlayer

    private var currentLevel: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //deleteGameList()
        gameHistoryRepository = GameHistoryRepository(this)

        currentLevel = getIntent().getIntExtra("level", 1);
        levelDifferences()
        initViews()

        drumSound = MediaPlayer.create(this, R.raw.drum)
        drumSound2 = MediaPlayer.create(this, R.raw.drum2)
        drumSmallSound = MediaPlayer.create(this, R.raw.drum_small)
        drumSmallSound2 = MediaPlayer.create(this, R.raw.drum_small2)
        tssSound = MediaPlayer.create(this, R.raw.tss_left)
        tssSound2 = MediaPlayer.create(this, R.raw.tss_right)

        //hide hidden objects
        hiddenTest.visibility = View.GONE
        ivStar1hidden.visibility = View.GONE
        ivStar2hidden.visibility = View.GONE
        ivStar3hidden.visibility = View.GONE

        btnRetry.visibility = View.GONE
        btnLevelSelect.visibility = View.GONE
        btnTitleScreen.visibility = View.GONE

        showCombination()
    }

    private fun makeButtonsVisible(){
        btnBigDrumLeft.visibility = View.VISIBLE
        btnBigDrumRight.visibility = View.VISIBLE
        if(currentLevel > 1){
            btnSmallDrumLeft.visibility = View.VISIBLE
            btnSmallDrumRight.visibility = View.VISIBLE
        }
        if(currentLevel > 2) {
            btnTssLeft.visibility = View.VISIBLE
            btnTssRight.visibility = View.VISIBLE
        }
    }

    private fun makeButtonsInvisible(){
        btnBigDrumLeft.visibility = View.INVISIBLE
        btnBigDrumRight.visibility = View.INVISIBLE
        btnSmallDrumLeft.visibility = View.INVISIBLE
        btnSmallDrumRight.visibility = View.INVISIBLE
        btnTssLeft.visibility = View.INVISIBLE
        btnTssRight.visibility = View.INVISIBLE
    }

    private fun levelDifferences(){
        makeButtonsInvisible()
        when (currentLevel) {
            1 -> {
                backgroundMainActivity.setBackground(ContextCompat.getDrawable(this, R.drawable.background))
                computerOptions = arrayOf(0,1)
                makeButtonsVisible()
            }
            2 -> {
                backgroundMainActivity.setBackground(ContextCompat.getDrawable(this, R.drawable.background2))
                computerOptions = arrayOf(0,1,2,3)
                makeButtonsVisible()
            }
            3 -> {
                backgroundMainActivity.setBackground(ContextCompat.getDrawable(this, R.drawable.background3))
                computerOptions = arrayOf(0,1,2,3,4,5)
                makeButtonsVisible()
            }
        }
    }

    private fun deleteGameList() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameHistoryRepository.deleteAllProducts()
            }
        }
    }

    private fun initViews() {
        btnBigDrumLeft.setOnClickListener { playerInput(0) }
        btnBigDrumRight.setOnClickListener { playerInput(1) }
        btnSmallDrumLeft.setOnClickListener { playerInput(2) }
        btnSmallDrumRight.setOnClickListener { playerInput(3) }
        btnTssLeft.setOnClickListener { playerInput(4) }
        btnTssRight.setOnClickListener { playerInput(5) }

        btnRetry.setOnClickListener { onRetry() }
        btnLevelSelect.setOnClickListener { onLevelSelect() }
        btnTitleScreen.setOnClickListener { onTitleScreen() }
    }

    private fun onRetry() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun onLevelSelect() {
        finish()
        startActivity(Intent(this, LevelSelect::class.java))
    }

    private fun onTitleScreen() {
        finish()
        startActivity(Intent(this, TitleScreen::class.java))
    }

    private fun addStats(playerHandIndex: Int) {
        mainScope.launch {
            val currentTime = Calendar.getInstance().time
            val product = GameHistoryStats(

                level = currentLevel,
                points = points,
                date = currentTime,
                stars = starCount
            )

            withContext(Dispatchers.IO) {
                gameHistoryRepository.insertProduct(product)
            }
        }
    }

    private fun showCombination() {
        if (wrongAnswers >= 3)
            gameOver()
        else {
            showCombinationAnimation()
            playerCombination.clear()
            numberOfPlayerInputs = 0
        }
    }

    private fun clearAnimation(combination: Int){
        val max =  computerOptions.size - 1
        println("max: " + max )

        var buttons = arrayOf(btnBigDrumLeft, btnBigDrumRight)
        if(max > 2){
            buttons = arrayOf(btnBigDrumLeft, btnBigDrumRight, btnSmallDrumLeft, btnSmallDrumRight)
            if(max > 4){
                buttons = arrayOf(btnBigDrumLeft, btnBigDrumRight, btnSmallDrumLeft, btnSmallDrumRight, btnTssLeft, btnTssRight)
            }
        }

        for(i in 0..max){
            if(i != combination) {
                //use this so it works on emulators (fixes the async)
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    buttons[i].clearAnimation()
                })
            }
        }
    }

    private fun showCombinationAnimation() {
        val max =  computerOptions.size - 1
        println("max: " + max )

        var buttons = arrayOf(btnBigDrumLeft, btnBigDrumRight)
        if(max > 2){
            buttons = arrayOf(btnBigDrumLeft, btnBigDrumRight, btnSmallDrumLeft, btnSmallDrumRight)
            if(max > 4){
                buttons = arrayOf(btnBigDrumLeft, btnBigDrumRight, btnSmallDrumLeft, btnSmallDrumRight, btnTssLeft, btnTssRight)
            }
        }
        var sounds = arrayOf(drumSound, drumSound2, drumSmallSound, drumSmallSound2, tssSound, tssSound2)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        var random: Int = (0..max).random()
        computerCombination.add(computerOptions[random])

        GlobalScope.launch {
            var i = 1
            var currentSpeed = 1000f
            cantInput = true

            for (combination in computerCombination) {
                //speed up the game
                if (currentSpeed >= 800f)
                    currentSpeed -= speed
                delay(currentSpeed.toLong())

                //use this so it works on emulators (fixes the async)
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    buttons[combination].startAnimation((bounceAnimation))
                    sounds[combination].start()
                })

                clearAnimation(combination)
                i++
            }

            //Place a tiny delay here
            cantInput = false

        }
    }

    private fun playerInput(playerInputVariable: Int) {

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        var buttons = arrayOf(btnBigDrumLeft, btnBigDrumRight, btnSmallDrumLeft, btnSmallDrumRight, btnTssLeft, btnTssRight)
        var sounds = arrayOf(drumSound, drumSound2, drumSmallSound, drumSmallSound2, tssSound, tssSound2)

        if (!cantInput) {

            //use this so it works on emulators (fixes the async)
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                buttons[playerInputVariable].startAnimation((bounceAnimation))
                sounds[playerInputVariable].start()
            })

            numberOfPlayerInputs++

            playerCombination.add(playerInputVariable)

            if (numberOfPlayerInputs == computerCombination.size) {
                if (playerCombination == computerCombination) {
                    correctAnswers++

                    points = correctAnswers * 10
                    if (points != 0) {
                        pointsTxt.text = points.toString()
                    } else {
                        pointsTxt.text = "0"
                    }
                    starCalculations()
                } else {
                    wrongAnswers++
                    when (wrongAnswers) {
                        1 -> cross.setImageResource(R.drawable.rock)
                        2 -> cross1.setImageResource(R.drawable.rock)
                        3 -> cross2.setImageResource(R.drawable.rock)
                    }
                }
                showCombination()
            }
        }
    }

    private fun starCalculations() {
        if (points >= 30) {
            starCount = 1
            if (points >= 50) {
                starCount = 2
                if (points >= 70)
                    starCount = 3
            }
        } else
            starCount = 0

        when (starCount) {
            1 -> {
                ivStar1g.setImageResource(R.drawable.star)
                ivStar1hidden.setImageResource(R.drawable.star)
            }
            2 -> {
                ivStar1g.setImageResource(R.drawable.star)
                ivStar2g.setImageResource(R.drawable.star)
                ivStar1hidden.setImageResource(R.drawable.star)
                ivStar2hidden.setImageResource(R.drawable.star)
            }
            3 -> {
                ivStar1g.setImageResource(R.drawable.star)
                ivStar2g.setImageResource(R.drawable.star)
                ivStar3g.setImageResource(R.drawable.star)
                ivStar1hidden.setImageResource(R.drawable.star)
                ivStar2hidden.setImageResource(R.drawable.star)
                ivStar3hidden.setImageResource(R.drawable.star)
            }
            else -> {
                print("starCount is wrong")
            }
        }
    }

    private fun gameOver() {
        var playerResourceID: Int = 1
        addStats(playerResourceID)
        gameOverPopUp()
    }

    private fun gameOverPopUp() {
        hiddenTest.visibility = View.VISIBLE
        ivStar1hidden.visibility = View.VISIBLE
        ivStar2hidden.visibility = View.VISIBLE
        ivStar3hidden.visibility = View.VISIBLE
        btnRetry.visibility = View.VISIBLE
        btnLevelSelect.visibility = View.VISIBLE
        btnTitleScreen.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    companion object {
        const val PRODUCT_EXTRA = "PRODUCT_EXTRA"
    }
}