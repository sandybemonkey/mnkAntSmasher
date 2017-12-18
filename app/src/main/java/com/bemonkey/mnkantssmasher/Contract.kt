package com.bemonkey.mnkantssmasher

/**
 * This Interface will model the responsabilities for the game
 *
 * Created by bmnk on 13/12/17.
 * @author Sandy PIERRE - BeMonKey
 */
interface Contract {
    /**
     * Describe the responsibilities for the graphic layer
     */
    interface GameView {

        /**
         * Show an Ant on the screen
         * @param ant of type Ant
         */
        fun showAnt(ant: Ant)

        /**
         * Hide an Ant on the screen
         * @param ant of type Ant
         */
        fun hideAnt(ant: Ant)

        /**
         * Show the score on the screen
         */
        fun showScore(score: Int)

        /**
         * Manage the intro text visibility
         * @param visibility of type Boolean
         */
        fun setIntroTextVisibility(visibility: Boolean)

        /**
         * Manage the game over text visibility
         * @param visibility of type Boolean
         */
        fun setGameOverTextVisibility(visibility: Boolean)

        /**
         * Manage the play button visibility
         * @param visibility of type Boolean
         */
        fun setPlayButtonVisibility(visibility: Boolean)

        /**
         * Manage a new game
         */
        fun clearView()
    }

    /**
     * Describe the responsibilities for the controller/Presenter layer
     */
    interface GameEngine {

        /**
         * Notify the game engine that view is ready for interactions
         * @param view of type GameView
         */
        fun onGameViewReady(view: GameView)

        /**
         * Notify the game engine that view is destroyed
         */
        fun onViewDestroyed(view: GameView)

        /**
         * Notify the game engine that the play button was clicked
         */
        fun onPlayButtonClicked()

        /**
         * Notify the game engine that the player touched an Ant
         * @param ant of type Ant
         */
        fun onAntClicked(ant: Ant)
    }
}