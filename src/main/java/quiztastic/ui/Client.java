package quiztastic.ui;

import quiztastic.core.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Player player;
    private int score;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.score=0;
        this.player=null;
        this.in=new Scanner(socket.getInputStream());
        this.out=new PrintWriter(socket.getOutputStream(),true);


    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Scanner getIn() {
        return in;
    }

    public void setIn(Scanner in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }
}

