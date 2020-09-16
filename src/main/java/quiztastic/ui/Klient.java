package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.entries.RunTUI;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Klient implements Runnable{
    private final Scanner in;
    private final PrintWriter out;

    public Klient(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        out.println("connected to server");
        out.flush();
        //new Protocol(in,out ).run();

       out.println( Quiztastic.getInstance().getCurrentGame().getBoard().toString());
        out.flush();
       while(true){}
      //  in.close();
      //  out.close();
    }
}
