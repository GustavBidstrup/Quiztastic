package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Player;
import quiztastic.domain.Game;



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
        player.setName(name);

        client.getOut().println("you are now in the game " + player);
        client.setPlayer(player);


        client.getOut().println("Reglerne i Quiztastic:");
        client.getOut().println("Vi spiller 3 runder, 3 spillere.");
        client.getOut().println("en runde består af:");
        client.getOut().println("Spillet går i gang når der er 3 spillere");
        client.getOut().println("Spillerne skiftes til at vælge spørgsmål. Man vælger ved at skrive f.eks a100.");
        client.getOut().println("Så skal alle Buzze (trykke på return) den første som buzzer, får lov at svare på spørgsmålet");
        client.getOut().println("Der svares og tildeles point , ved korrekt svar lægges de til ved forkert trækkes de fra");
        client.getOut().println("Efter 3 runder har spileren med flest point vundet.");


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
