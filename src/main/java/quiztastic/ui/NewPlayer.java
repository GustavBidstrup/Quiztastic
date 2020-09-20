package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Player;
import quiztastic.domain.Game;

import java.io.PrintWriter;
import java.util.Scanner;

public class NewPlayer implements Runnable{
    private Client client;
    private Game game= Quiztastic.getInstance(Server.filename).getCurrentGame();

    public NewPlayer(Client client) {
       this.client=client;
    }


    @Override
    public void run() {
        Player player = new Player();
        client.getOut().println("What is your name?");
        String name = fetchLine(client);
       // name=fetchLine();
        player.setName(name);
        client.getOut().println("you are now in the game " + player);


        client.getOut().println("spilleregelr");

        client.setPlayer(player);

    }
    private String fetchLine(Client client) {
        client.getOut().print("> ");
        String line = client.getIn().nextLine().strip();
        int start=0;
        //fjern foranliggende der ikke er bogstaver
      /* for (int i = 0; i <line.length() ; i++) {
            if(line.charAt(i)<'a'||line.charAt(i)>'Z'){
                start=i;
            }
            line=line.substring(start);
        }*/
        return line;
    }
}
