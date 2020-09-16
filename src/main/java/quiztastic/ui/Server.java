package quiztastic.ui;

import quiztastic.domain.Game;
import quiztastic.entries.RunGame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        final int port = 6060;
        final ServerSocket serverSocket = new ServerSocket(port);
        RunGame runGame=new RunGame();
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket.getRemoteSocketAddress()+" connected");
            //strart Thread
            Klient klient=new Klient(new Scanner(socket.getInputStream()), new PrintWriter(socket.getOutputStream()));
            Thread thread=new Thread(klient);
            thread.start();


          //  new Protocol(new Scanner(socket.getInputStream()), new PrintWriter(socket.getOutputStream())).run();

        }
    }

}
