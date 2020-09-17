package quiztastic.ui;

import quiztastic.core.Player;
import quiztastic.domain.Game;

public class DataRace extends Player implements Runnable {

    // State 1: Game round starts
        // this.player choose question

    // State 2: Player buzz<enter> game block for other players

    // State 3: this.player answer true or false

    // State 4: if true -> return to State 1
        // collect score to this.player

    // State 5: if false -> unlock other players to answer

    // State 6: -> State 2

    Game game;
    private static Object lock = new Object();


    public static void main(String[] args) throws InterruptedException {


    }

    @Override
    public void run() {

    }
}

