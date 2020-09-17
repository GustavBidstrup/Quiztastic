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
    private Player klientPlayer;
    public Klient(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        out.println("connected to server");
        out.flush();
       klientPlayer=setNewPlayer();
       out.println(klientPlayer);
       while(game.numberOffPlayers()<2){}
       out.println("ready to play");

        drawBoard(game.getBoard());
        out.flush();
       for (int i=0;i<15;i++){
          // choseQuestion();
           //buzz();
          answer("a100");

       }


       drawBoard(game.getBoard());
       out.flush();
    }

    private void answer(String chosenQuestion) {

       
        System.out.println(klientPlayer.getName());
        if (klientPlayer.getName().equals("jens")) {
            int categoryNumber = (int) chosenQuestion.charAt(0) - (int) 'a';
            int questionNumber = Integer.parseInt(chosenQuestion.substring(1)) / 100 - 1;
            out.println(game.getQuestionText(categoryNumber, questionNumber));
            out.flush();
            String answer = fetchLine();
            String correctAnswer = game.answerQuestion(categoryNumber, questionNumber, answer);
            if (correctAnswer == null) {
                out.println("Correct, You got " + (questionNumber + 1) * 100 + " points");
            } else {
                out.println("Wrong, the answer was: " + correctAnswer);
            }
            out.flush();

        }else{

        }
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
            str=str+c+": "+String.format("%-12s",group.getCategory().getName());
            c++;
        }

        str=str+"\n";
        for (int questionNumber=0;questionNumber<5;questionNumber++){
            for (int groupNumber=0;groupNumber<6;groupNumber++){
                if (game.isAnswered(groupNumber,questionNumber)){
                    str=str+String.format("%-15s","---");
                }else {
                    str = str + String.format("%-15d", (questionNumber + 1) * 100);

                }
            }
            str=str+"\n";

        }
        out.println(str);
    }
}
