package quiztastic.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class RunServer {
    public static void main(String[] args) throws IOException {
        final int port = 6060;
        final ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket.getRemoteSocketAddress()+" connected");
            new Protocol(new Scanner(socket.getInputStream()), new PrintWriter(socket.getOutputStream())).run();
            socket.close();
        }
    }

}
