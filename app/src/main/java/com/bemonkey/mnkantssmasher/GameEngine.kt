package com.bemonkey.mnkantssmasher

import android.os.Handler
import android.util.Log
import java.util.*

/**
 * Created by bmnk on 13/12/17.
 */
class GameEngine: Contract.GameEngine {
    // setting a variable of type gameView
    private var gameView: Contract.GameView? = null

    // property to keep tracks of the score after an ant is touched/clicked
    private var score: Int = 0

    // Create a data stucture wich contains every ant that going to be visible on the screen
    // it's going to be a list of ants
    private var ants: MutableCollection<Ant> = ArrayList()

    // creating a new random property and it will be use to define the x and y position of an ant
    private val random: Random = Random()

    // setting the mecanisme to show ant on screen using Handler
    private val handler: Handler = Handler()

    // implementing runnable will be responsible of spawning a single ant on screen
    private val ShowAntRunnable: Runnable = object: Runnable {
        override fun run() {
            val ant = createNewAnt()
            // tchecking if there is an ant in our ants list
            // if ants list is no empty we stop the game
            val isGameOver = checkGameOver()

            if (!isGameOver) {
                // adding the ant to our list
                ants.add(ant)

                // showing the ant on the screen
                gameView?.showAnt(ant)
                Log.d("Ant presenter", "new ant")

                // using handler to repeat that step with the delay of recuring
                handler.postDelayed(this, 1500)

            } else {
                // stopping the spawning mechanisme
                handler.removeCallbacks(this)
                Log.d("Ant presenter", "Game Over")

                // clearing the game view
                gameView?.clearView()

                // showing game over text
                gameView?.setGameOverTextVisibility(true)

                // show play button
                gameView?.setPlayButtonVisibility(true)
                Log.d("Ant presenter", "setting text to true")
            }
        }
    }

    private fun checkGameOver(): Boolean {
        return ants.isNotEmpty()
    }

    private fun createNewAnt(): Ant {
        // for the id we using the ants list size and incrementing it
        val id = ants.size + 1
        val x = getAntXposition()
        val y = getAntYposition()
        return Ant( id, x, y)
    }

    /**
     * returns Ant's vertical position as a screen height ratio, in range 0.0-1.0
     */
    private fun getAntYposition(): Double {
        return random.nextDouble()
    }

    /**
     * returns Ant's horizontal position as a screen width ratio, in range 0.0-1.0
     */
    private fun getAntXposition(): Double {
        return random.nextDouble()
    }
    override fun onGameViewReady(view: Contract.GameView) {
        gameView = view
        gameView?.setIntroTextVisibility(true)
        gameView?.setGameOverTextVisibility(false)
    }

    override fun onViewDestroyed(view: Contract.GameView) {
        gameView = null
    }

    override fun onPlayButtonClicked() {
        //hide play button
        gameView?.setPlayButtonVisibility(false)

        //clear the gameView
        gameView?.clearView()

        //clear our list of ants
        ants.clear()

        //here we running our runnable
        ShowAntRunnable.run()

        // Set initial score
        score = 0
        // showing the score on the screen
        gameView?.showScore(score)
    }

    override fun onAntClicked(ant: Ant) {
        // remove ant from list of ants
        ants.remove(ant)

        // hide ant on gamescreen
        gameView?.hideAnt(ant)

        // incrementing the score property
        score++

        // showing the score on the screen
        gameView?.showScore(score)

    }
}