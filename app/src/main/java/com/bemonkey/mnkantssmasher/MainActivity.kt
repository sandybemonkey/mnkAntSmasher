package com.bemonkey.mnkantssmasher

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by bmnk on 13/12/17.
 */
class MainActivity: AppCompatActivity(), Contract.GameView {

    // Game engine
    private val engine: GameEngine

    init {
        // initialize game engine variable
        this.engine = GameEngine()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(Tb_toolbar)

        Fb_play_button?.setOnClickListener { engine.onPlayButtonClicked() }

        val d = Drawable.createFromStream(assets.open("bg.jpeg"), null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Fl_game_layout.background = d
        }

        // Notify game engine that the view is ready
        this.engine.onGameViewReady(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Notify game engine that the view is destroyed
        this.engine.onViewDestroyed(this)
    }

    override fun showAnt(ant: Ant) {
        // create an imageView for our ant
        val antView = ImageView(this)

        // set the drawable for the imageView
        antView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_ant))

        // setting the scaleType for our drawable
        antView.scaleType = ImageView.ScaleType.FIT_CENTER

        // referencing the imageView to the current ant object passed in
        antView.tag = ant

        // support for when ant is clicked
        antView.setOnClickListener (object: View.OnClickListener {
            override fun onClick(v: View?) {
                val ant = v!!.tag as Ant
                engine.onAntClicked(ant)
            }
        })

        // adapting ant image to different screen size
        val antSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                56f,
                resources.displayMetrics
        )
        val layoutParams = FrameLayout.LayoutParams(antSize.toInt(), antSize.toInt())

        // get screen Width and Height to set the positon in the game layout
        val screenWidth = Fl_game_layout!!.width - antSize
        val screenHeight = Fl_game_layout!!.height - antSize

        // set position of the ant in the game layout
        layoutParams.leftMargin = (ant.x * screenWidth).toInt()
        layoutParams.topMargin = (ant.y * screenHeight).toInt()

        // Add the ant to the view and the params is the size of the view  adapting  to any devices size
        Fl_game_layout.addView(antView,layoutParams)
    }

    override fun hideAnt(antToHide: Ant) {
        val antViewsCount: Int? = Fl_game_layout.childCount
        antViewsCount?.let {
            // iterate over the sub views to find the one corresponding to the ant
            for (i: Int in 0..antViewsCount) {
                // getting the view at the current index
                val view = Fl_game_layout?.getChildAt(i)

                // getting the tag of that current view
                val ant = view?.tag

                // compare the current view tag to the ant tag passed in param
                if (antToHide == (ant)) {
                    // if match remove ant from game screen
                    Fl_game_layout.removeView(view)

                    // when we found the right view we break the loop
                    break
                }
            }
        }
    }

    override fun showScore(score: Int) {
        Tv_score.text = "Points: $score"
    }

    override fun setIntroTextVisibility(visibility: Boolean) {
        Tv_intro_text?.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun setGameOverTextVisibility(visibility: Boolean) {
        Tv_game_over?.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun setPlayButtonVisibility(visibility: Boolean) {
        Fb_play_button?.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun clearView() {
        // we remove all views from the game screen
        Fl_game_layout?.removeAllViews()
        Tv_intro_text?.visibility = View.GONE
        Tv_game_over?.visibility = View.GONE
    }
}