package com.example.shoppinglistkotlin

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val products = arrayListOf<Product>()
    private val productAdapter = ProductAdapter(products)

    private lateinit var productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var hand: String = "paper"
    private var cHand: String = "rock"

    private var playerHandInt: Int = 1
    private var computerHandInt: Int = 1
    private var scoreInt: Int = 0

    //var randomStart: Int = (0..2).random()
    private val computerOptions: Array<Int> =
        arrayOf(0,1,2,3,4,5)
    private val computerCombination: ArrayList<Int> = arrayListOf()//computerOptions[randomStart]

    val playerCombination: ArrayList<Int> = arrayListOf()

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

    private fun deleteGameList() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
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

    override fun onBackPressed() {

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

    private fun addProduct(playerHandIndex: Int, computerHandIndex: Int) {
        //private fun addProduct(points: Int) {
        mainScope.launch {
            val currentTime = Calendar.getInstance().time
            val product = Product(

                computerHand = points,
                playerHand = playerHandIndex,
                //winner = winlose.text.toString(),
                date = currentTime,
                stars = starCount
            )

            withContext(Dispatchers.IO) {
                productRepository.insertProduct(product)
            }
        }
    }

    private fun showCombination() {
        if (wrongAnswers >= 3)
            gameOver()
        else {
            showCombinationAnimation()
            //winlose.text = computerCombination.toString()

            //vs.text = wrongAnswers.toString()


            playerCombination.clear()
            numberOfPlayerInputs = 0

        }
    }

    private fun showCombinationAnimation() {
        val buttons = arrayOf(btnBigDrumLeft, btnBigDrumRight, btnSmallDrumLeft, btnSmallDrumRight, btnTssLeft, btnTssRight)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        var random: Int = (0..5).random()
        computerCombination.add(computerOptions[random])

        GlobalScope.launch {
            var i = 1
            var currentSpeed = 1000f
            cantInput = true


            for (combination in computerCombination) {
                if (currentSpeed <= 400f)
                    currentSpeed -= speed
                delay(currentSpeed.toLong())
                //stopSound()

                println("combinatie: " + combination)
                if (combination == 0) {
                   // buttons[combination].startAnimation((bounceAnimation))

                    btnBigDrumLeft.startAnimation(bounceAnimation)
                    btnBigDrumRight.clearAnimation()
                    btnSmallDrumLeft.clearAnimation()
                    btnSmallDrumRight.clearAnimation()
                    btnTssLeft.clearAnimation()
                    btnTssRight.clearAnimation()
                    drumSound.start()

                }
                if (combination == 1) {
                    btnBigDrumRight.startAnimation(bounceAnimation)
                    btnBigDrumLeft.clearAnimation()
                    btnSmallDrumLeft.clearAnimation()
                    btnSmallDrumRight.clearAnimation()
                    btnTssLeft.clearAnimation()
                    btnTssRight.clearAnimation()
                    drumSound2.start()

                }
                if (combination == 2) {
                    btnSmallDrumLeft.startAnimation(bounceAnimation)
                    btnBigDrumLeft.clearAnimation()
                    btnBigDrumRight.clearAnimation()
                    btnSmallDrumRight.clearAnimation()
                    btnTssLeft.clearAnimation()
                    btnTssRight.clearAnimation()
                    drumSmallSound.start()

                }

                if (combination == 3) {
                    btnSmallDrumRight.startAnimation(bounceAnimation)
                    btnBigDrumLeft.clearAnimation()
                    btnBigDrumRight.clearAnimation()
                    btnSmallDrumLeft.clearAnimation()
                    btnTssLeft.clearAnimation()
                    btnTssRight.clearAnimation()
                    drumSmallSound2.start()

                }
                if (combination == 4) {
                    btnTssLeft.startAnimation(bounceAnimation)
                    btnBigDrumLeft.clearAnimation()
                    btnBigDrumRight.clearAnimation()
                    btnSmallDrumLeft.clearAnimation()
                    btnSmallDrumRight.clearAnimation()
                    btnTssRight.clearAnimation()
                    tssSound.start()
                }
                if (combination == 5) {
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

            //Place a tiny delay here
            cantInput = false

        }
    }

    private fun stopSound() {
        println("stopSound isPlaying")

        if (drumSound.isPlaying) {
            println("drumSound isPlaying")
            drumSound.reset()
            drumSound.seekTo(0)
            drumSound.stop()
        }

        if (drumSound2.isPlaying){
            println("drumSound2 isPlaying")
            drumSound2.reset()
            drumSound2.seekTo(0)
            drumSound2.stop()
        }

        if (drumSmallSound.isPlaying){
            println("drumSmallSound isPlaying")
            drumSmallSound.reset()
            drumSmallSound.seekTo(0)
            drumSmallSound.stop()
        }

        if (drumSmallSound2.isPlaying){
            println("drumSmallSound2 message")
            drumSmallSound2.reset()
            drumSmallSound2.seekTo(0)
            drumSmallSound2.stop()
        }

        if (tssSound.isPlaying){
            println("tssSound isPlaying")
            tssSound.reset()
            tssSound.seekTo(0)
            tssSound.stop()
        }

        if (tssSound2.isPlaying){
            println("tssSound2 isPlaying")
            tssSound2.reset()
            tssSound2.seekTo(0)
            tssSound2.stop()
        }
    }


//    private fun stopSound(){
//        if(drumSound.isPlaying)
//            drumSound.stop()
//
//        if(drumSound2.isPlaying)
//            drumSound2.stop()
//
//        if(drumSmallSound.isPlaying)
//            drumSmallSound.stop()
//
//        if(drumSmallSound2.isPlaying)
//            drumSmallSound2.stop()
//
//        if(tssSound.isPlaying)
//            tssSound.stop()
//
//        if(tssSound2.isPlaying)
//            tssSound2.stop()
//    }


    private fun playerInput(playerInputVariable: Int) {

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        if (!cantInput) {
            println("cant input text: " + cantInput)

            when (playerInputVariable) {
                0 -> {
                    drumSound.start()
                    btnBigDrumLeft.startAnimation(bounceAnimation)
                }
                1 -> {
                    drumSound2.start()
                    btnBigDrumRight.startAnimation(bounceAnimation)
                }
                2 -> {
                    drumSmallSound.start()
                    btnSmallDrumLeft.startAnimation(bounceAnimation)
                }
                3 -> {
                    drumSmallSound2.start()
                    btnSmallDrumRight.startAnimation(bounceAnimation)
                }
                4 -> {
                    tssSound.start()
                    btnTssLeft.startAnimation(bounceAnimation)
                }
                5 -> {
                    tssSound2.start()
                    btnTssRight.startAnimation(bounceAnimation)
                }
            }

//            if (playerInputVariable == "0"){
//                drumSound.start()
//                btnBigDrumLeft.startAnimation(bounceAnimation)
//            }
//            if (playerInputVariable == "1"){
//                drumSound2.start()
//                btnBigDrumRight.startAnimation(bounceAnimation)
//            }
//            if (playerInputVariable == "2"){
//                drumSmallSound.start()
//                btnSmallDrumLeft.startAnimation(bounceAnimation)
//            }
//            if (playerInputVariable == "3"){
//                drumSmallSound2.start()
//                btnSmallDrumRight.startAnimation(bounceAnimation)
//            }
//            if (playerInputVariable == "4"){
//                tssSound.start()
//                btnTssLeft.startAnimation(bounceAnimation)
//            }
//            if (playerInputVariable == "5"){
//                tssSound2.start()
//                btnTssRight.startAnimation(bounceAnimation)
//            }

            numberOfPlayerInputs++

            playerCombination.add(playerInputVariable)
            //println(playerCombination + " EN " + computerCombination + "computersize: " + computerCombination.size)
            //println(computerCombination)

            if (numberOfPlayerInputs == computerCombination.size) {
                if (playerCombination == computerCombination) {
                    correctAnswers++
                    //score.text = "goed"

                    points = correctAnswers * 10
                    if (points != 0) {
                        pointsTxt.text = points.toString()
                    } else {
                        pointsTxt.text = "0"
                    }
                    starCalculations()
                } else {
                    wrongAnswers++
                    //score.text = "fout het was " + computerCombination[computerCombination.lastIndex]
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

    fun starCalculations() {
        if (points >= 30) {//30
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
            else -> { // Note the block
                print("starCount is wrong")
            }
        }
        println("starCount: " + starCount)
    }

    private fun gameOver() {
        //score.text = "You lose"
        //vs.text = "Yoooo"

        var playerResourceID: Int = 1
        var computerResourceID: Int = 1

        addProduct(playerResourceID, computerResourceID)
        gameOverPopUp()
        //gameOverPopUp()
    }

    private fun gameOverPopUp() {
        //val profileActivityIntent = Intent(this, GameHistory::class.java)
        //startActivity(profileActivityIntent)
        //true
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