package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Player;
import quiztastic.domain.Game;

import java.io.PrintWriter;
import java.util.Scanner;

public class NewPlayer implements Runnable{
    private int id;
    private final Scanner in;
    private final PrintWriter out;
    private Game game= Quiztastic.getInstance().getCurrentGame();

    public NewPlayer(int id, Scanner in, PrintWriter out) {
        this.id = id;
        this.in = in;
        this.out = out;
    }


    @Override
    public void run() {
        Player player = new Player();
        out.println("What is your name?");
        String name = fetchLine();
        name=fetchLine();
        player.setName(name);
        out.println("you are now in the game " + player);
        out.flush();
        game.getPlayers().get(id).setName(name);

    }
    private String fetchLine() {
        out.print("> ");
        out.flush();
        String line = in.nextLine().strip();
        return line;
    }
}
