package quiztastic.ui;

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
        in.close();
        out.close();
    }
}
