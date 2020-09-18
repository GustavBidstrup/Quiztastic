package quiztastic.ui;

import quiztastic.core.Player;
import quiztastic.domain.Game;

import java.util.LinkedList;
import java.util.List;

public class DataRace implements Runnable {

    // State 1: Game round starts
    // this.player choose question

    // State 2: Player buzz<enter> game block for other players

    // State 3: this.player answer true or false

    // State 4: if true -> return to State 1
    // collect score to this.player

    // State 5: if false -> unlock other players to answer

    // State 6: -> State 2


    public int allBuzz() throws InterruptedException {
        int x = 0;


        // List<Thread> threads = List.of(new Thread(), new Thread(d2));

        return x;
    }

    public static void main(String[] args) throws InterruptedException {
        DataRace x = new DataRace();
        x.allBuzz();


    }

    @Override
    public void run() {

    }
}

    /*
    @Override
    public void run() {

    }
}

     */

