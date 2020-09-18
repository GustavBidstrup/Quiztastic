package quiztastic.ui;

import java.util.Scanner;
import quiztastic.ui.Server;
import quiztastic.domain.Game;
public class Buzz implements Runnable {

    int playerNumber;
    Scanner in;

    public Buzz(int playerNumber, Scanner in) {
        this.in = in;
        this.playerNumber = playerNumber;
    }

    @Override
    public void run() {

        while ((!in.hasNextLine())) {

        }
            Server.playerToAnswer= playerNumber;

            Server.someoneBuzzed = true;


        System.out.println(Server.playerToAnswer+" "+Server.someoneBuzzed);
    }
}
