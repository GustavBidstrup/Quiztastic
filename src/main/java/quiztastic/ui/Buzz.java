package quiztastic.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;
import quiztastic.ui.Server;
import quiztastic.domain.Game;
public class Buzz implements Runnable {

    Client client;
    Scanner in;

    public Buzz(Client client) {
        this.in = client.getIn();
        this.client=client;
    }

    @Override
    public void run() {
        while ((!Server.someoneBuzzed)) {
            if(in.hasNextLine()) {
                if(!Server.someoneBuzzed){
                Server.playerToAnswer = client;
                Server.someoneBuzzed = true;
            }

        }
        in.nextLine();}
        System.out.println(client.getPlayer().getName()+" out");
    }


}
