package quiztastic.entries;

import quiztastic.ui.Protocol;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class RunTUI {
    private final Socket socket;

    public RunTUI(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
        final int port = 6060;
        final ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        new Protocol(new Scanner(socket.getInputStream()), new PrintWriter(socket.getOutputStream())).run();
    }
}
