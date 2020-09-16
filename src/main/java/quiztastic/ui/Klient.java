package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
import quiztastic.core.Player;
import quiztastic.domain.Game;

import java.io.PrintWriter;
import java.util.Scanner;

public class Klient implements Runnable{
    private final Scanner in;
    private final PrintWriter out;
    private Game game=Quiztastic.getInstance().getCurrentGame();
    public Klient(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        out.println("connected to server");
        out.flush();
       out.println(setNewPlayer());
       while(game.numberOffPlayers()<2){}
       out.println("ready to play");

       /*
       for (int i=0;i>15;i++){
           choseQuestion();
           buzz();
           answer();
       }
        */


       drawBoard(game.getBoard());
       out.flush();
    }

    private String fetchLine() {
        out.print("> ");
        out.flush();
        String line = in.nextLine().strip();
        return line;
    }
    private Player setNewPlayer() {
        Player player = new Player();
        out.println("What is your name?");
        String name = fetchLine();
        name=fetchLine();
        player.setName(name);
        out.println("you are now in the game " + player);
        out.flush();
        game.addPlayer(player);
        return player;
    }
    private void drawBoard(Board board){
        String str="";

        char c='A';
        for (Board.Group group: board.getGroups()){
            str=str+c+": "+String.format("%-27s",group.getCategory().getName());
            c++;
        }

        str=str+"\n";
        for (int questionNumber=0;questionNumber<5;questionNumber++){
            for (int groupNumber=0;groupNumber<6;groupNumber++){
                if (game.isAnswered(groupNumber,questionNumber)){
                    str=str+String.format("%-30s","---");
                }else {
                    str = str + String.format("%-30d", (questionNumber + 1) * 100);

                }
            }
            str=str+"\n";

        }
        out.println(str);
    }
}
