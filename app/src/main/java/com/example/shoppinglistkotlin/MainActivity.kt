package com.example.shoppinglistkotlin

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils

import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var hand: String = "paper"
    private var cHand: String = "rock"

    private var playerHandInt: Int = 1
    private var computerHandInt: Int = 1
    private var scoreInt: Int = 0

    //var randomStart: Int = (0..2).random()
    private val computerOptions: Array<String> = arrayOf("0", "1", "2", "3", "4", "5") // was eerst 0 - 2
    private val computerCombination: ArrayList<String> = arrayListOf()//computerOptions[randomStart]

    val playerCombination: ArrayList<String> = arrayListOf()

    var numberOfPlayerInputs: Int = 0

    var cantInput: Boolean = true

    var speed: Float = 75f
    var wrongAnswers: Int = 0
    var correctAnswers: Int = 0

    var points: Int = 0
    var starCount: Int = 0


    private lateinit var mp: MediaPlayer
    private lateinit var drumSound: MediaPlayer
    private lateinit var drumSound2: MediaPlayer
    private lateinit var drumSmallSound: MediaPlayer
    private lateinit var drumSmallSound2: MediaPlayer
    private lateinit var tssSound: MediaPlayer
    private lateinit var tssSound2: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //deleteGameList()

        productRepository = ProductRepository(this)
        initViews()

        //sound

        mp = MediaPlayer.create(this, R.raw.testsound)

        showCombination()
    }

    private fun deleteGameList() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
        }
    }

    private fun initViews() {
        //btnRock.setOnClickListener { onRockClick() }

        btnBigDrumLeft.setOnClickListener { playerInput("0") }
        btnBigDrumRight.setOnClickListener { playerInput("1") }
        btnSmallDrumLeft.setOnClickListener { playerInput("2") }
        btnSmallDrumRight.setOnClickListener { playerInput("3") }
        btnTssLeft.setOnClickListener { playerInput("4") }
        btnTssRight.setOnClickListener { playerInput("5") }


        //btnPaper.setOnClickListener { onPaperClick() }
        //btnScissors.setOnClickListener { onScissorsClick() }
    }
    private fun addProduct(playerHandIndex: Int, computerHandIndex: Int) {
    //private fun addProduct(points: Int) {
        mainScope.launch {
            val currentTime = Calendar.getInstance().time
            val product = Product(

                computerHand = points,
                playerHand = playerHandIndex,
                winner = winlose.text.toString(),
                date = currentTime
            )

            withContext(Dispatchers.IO) {
                productRepository.insertProduct(product)
            }
        }
    }

    private fun computer() {
        computerHandInt = (1..3).random()

        cHand = when (computerHandInt) {
            1 -> "rock"
            2 -> "paper"
            else -> "scissors"
        }

        playerHandInt = when (hand) {
            "rock" -> 1
            "paper" -> 2
            else -> 3
        }

        var playerResourceID: Int
        var computerResourceID: Int

        playerResourceID = when (hand) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            else -> R.drawable.scissors
        }

        computerResourceID = when (cHand) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            else -> R.drawable.scissors
        }

        if (cHand == hand) {
            scoreInt++
            score.text = scoreInt.toString()
        } else {
            score.text = " "
        }

        playerHand.setImageResource(playerResourceID)
        computerHand.setImageResource(computerResourceID)
/*
        if(cHand == hand)
            winlose.text = "Draw"
        if(cHand == "rock" && hand == "paper" || cHand == "paper" && hand == "scissors" || cHand == "scissors" && hand == "rock")
            winlose.text = "You win!"
        if(hand == "rock" && cHand == "paper" || hand == "paper" && cHand == "scissors" || hand == "scissors" && cHand == "rock")
            winlose.text = "Computer wins!"

 */

    }

    private fun showCombination() {
        if(wrongAnswers >= 3)
            gameOver()
        else{
            showCombinationAnimation()
            winlose.text = computerCombination.toString()

            vs.text = wrongAnswers.toString()


            playerCombination.clear()
            numberOfPlayerInputs = 0
        }
    }

    private fun showCombinationAnimation() {


        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        var random: Int = (0..5).random()
        computerCombination.add(computerOptions[random])

        drumSound = MediaPlayer.create(this, R.raw.drum)
        drumSound2 = MediaPlayer.create(this, R.raw.drum2)
        drumSmallSound = MediaPlayer.create(this, R.raw.drum_small)
        drumSmallSound2 = MediaPlayer.create(this, R.raw.drum_small2)
        tssSound = MediaPlayer.create(this, R.raw.tss_left)
        tssSound2 = MediaPlayer.create(this, R.raw.tss_right)

        GlobalScope.launch {
            var i = 1
            var currentSpeed = 1000f
            cantInput = true


            for (combination in computerCombination) {
                if(currentSpeed <= 400f)
                currentSpeed -= speed
                delay(currentSpeed.toLong())
                stopSound()

                println("combinatie: " + combination)
                if (combination == "0") {
                    btnBigDrumLeft.startAnimation(bounceAnimation)
                    btnBigDrumRight.clearAnimation()
                    btnSmallDrumLeft.clearAnimation()
                    btnSmallDrumRight.clearAnimation()
                    btnTssLeft.clearAnimation()
                    btnTssRight.clearAnimation()
                    drumSound.start()

                }
                if (combination == "1") {
                    btnBigDrumRight.startAnimation(bounceAnimation)
                    btnBigDrumLeft.clearAnimation()
                    btnSmallDrumLeft.clearAnimation()
                    btnSmallDrumRight.clearAnimation()
                    btnTssLeft.clearAnimation()
                    btnTssRight.clearAnimation()
                    drumSound2.start()

                }
                if (combination == "2") {
                    btnSmallDrumLeft.startAnimation(bounceAnimation)
                    btnBigDrumLeft.clearAnimation()
                    btnBigDrumRight.clearAnimation()
                    btnSmallDrumRight.clearAnimation()
                    btnTssLeft.clearAnimation()
                    btnTssRight.clearAnimation()
                    stopSound()
                    drumSmallSound.start()

                }

                if (combination == "3") {
                    btnSmallDrumRight.startAnimation(bounceAnimation)
                    btnBigDrumLeft.clearAnimation()
                    btnBigDrumRight.clearAnimation()
                    btnSmallDrumLeft.clearAnimation()
                    btnTssLeft.clearAnimation()
                    btnTssRight.clearAnimation()
                    drumSmallSound2.start()

                }
                if (combination == "4") {
                    btnTssLeft.startAnimation(bounceAnimation)
                    btnBigDrumLeft.clearAnimation()
                    btnBigDrumRight.clearAnimation()
                    btnSmallDrumLeft.clearAnimation()
                    btnSmallDrumRight.clearAnimation()
                    btnTssRight.clearAnimation()
                    tssSound.start()
                }
                if (combination == "5") {
                    btnTssRight.startAnimation(bounceAnimation)
                    btnBigDrumLeft.clearAnimation()
                    btnBigDrumRight.clearAnimation()
                    btnSmallDrumLeft.clearAnimation()
                    btnSmallDrumRight.clearAnimation()
                    btnTssLeft.clearAnimation()
                    tssSound2.start()

                }
                i++

            }
            cantInput = false

        }
    }

    private fun stopSound(){
        if(drumSound.isPlaying)
            drumSound.stop()

        if(drumSound2.isPlaying)
            drumSound2.stop()

        if(drumSmallSound.isPlaying)
            drumSmallSound.stop()

        if(drumSmallSound2.isPlaying)
            drumSmallSound2.stop()

        if(tssSound.isPlaying)
            tssSound.stop()

        if(tssSound2.isPlaying)
            tssSound2.stop()
    }

    private fun playerInput(playerInputVariable: String) {

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        if (!cantInput) {
            println("cant input text: " + cantInput)
            stopSound() // remove if broken

            if (playerInputVariable == "0"){
                drumSound.start()
                btnBigDrumLeft.startAnimation(bounceAnimation)
            }
            if (playerInputVariable == "1"){
                drumSound2.start()
                btnBigDrumRight.startAnimation(bounceAnimation)
            }
            if (playerInputVariable == "2"){
                drumSmallSound.start()
                btnSmallDrumLeft.startAnimation(bounceAnimation)
            }
            if (playerInputVariable == "3"){
                drumSmallSound2.start()
                btnSmallDrumRight.startAnimation(bounceAnimation)
            }
            if (playerInputVariable == "4"){
                tssSound.start()
                btnTssLeft.startAnimation(bounceAnimation)
            }
            if (playerInputVariable == "5"){
                tssSound2.start()
                btnTssRight.startAnimation(bounceAnimation)
            }

            numberOfPlayerInputs++

            playerCombination.add(playerInputVariable)
            //println(playerCombination + " EN " + computerCombination + "computersize: " + computerCombination.size)
            //println(computerCombination)

            if (numberOfPlayerInputs == computerCombination.size) {
                if (playerCombination == computerCombination) {
                    correctAnswers++
                    score.text = "goed"

                    points = correctAnswers * 10
                    pointsTxt.text = points.toString()
                    starCalculations()
                } else {
                    wrongAnswers++
                    score.text = "fout het was " + computerCombination[computerCombination.lastIndex]
                    println(playerCombination + " EN !!!! " + computerCombination)
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

    fun starCalculations(){
        if(points >= 10){//30
            starCount = 1
            if(points >= 70){
                starCount = 2
                if(points >= 100)
                    starCount = 3
            }
        }
        else
            starCount = 0

        when (starCount) {
            1 -> ivStar1g.setImageResource(R.drawable.star)
            2 -> {ivStar1g.setImageResource(R.drawable.star)
                ivStar2g.setImageResource(R.drawable.star)}
            3 -> {ivStar1g.setImageResource(R.drawable.star)
                ivStar2g.setImageResource(R.drawable.star)
                ivStar3g.setImageResource(R.drawable.star)}
            else -> { // Note the block
                print("starCount is wrong")
            }
        }
        println("starCount: " + starCount)
    }

    private fun gameOver(){
        score.text = "You lose"
        vs.text = "Yoooo"

        var playerResourceID: Int = 1
        var computerResourceID: Int = 1

        addProduct(playerResourceID, computerResourceID)
        //gameOverPopUp()
    }

    private fun gameOverPopUp(){
        val profileActivityIntent = Intent(this, GameHistory::class.java)
        startActivity(profileActivityIntent)
        true
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

    companion object {
        const val PRODUCT_EXTRA = "PRODUCT_EXTRA"
    }
}