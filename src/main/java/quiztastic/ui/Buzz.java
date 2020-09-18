package quiztastic.ui;

import java.util.Scanner;

public class Buzz implements Runnable {

    int playerNumber;
    Scanner in;

    public Buzz(int playerNumber, Scanner in) {
        this.in = in;
        this.playerNumber = playerNumber;
    }

    @Override
    public void run() {

        while (!in.hasNext() ) { // || someomeBuzzed) {

        }
        if (in.hasNext()) {
            // someoneBuzzed = true;
            // playerToAnswer = playerNumber;
        }

    }
}
