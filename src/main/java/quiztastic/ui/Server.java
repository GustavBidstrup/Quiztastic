package quiztastic.ui;


import quiztastic.app.Quiztastic;
import quiztastic.core.Player;
import quiztastic.core.Question;

import quiztastic.domain.Game;
import quiztastic.entries.RunGame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Server {
    private final int port = 6060;
    private final ServerSocket serverSocket = new ServerSocket(port);
    private List<Socket> sockets = new ArrayList<>();
    private RunGame runGame = new RunGame();
    private Game game = Quiztastic.getInstance().getCurrentGame();
    private List<Thread> threads = new ArrayList<>();

    public Server() throws IOException, InterruptedException {
        while (true) {

            getPlayers(3);
            System.out.println(game.getPlayers());
            //starter 3 tråde med buzz returneer nr på første spiller
            int activePlayer = 1;
            String questionString;

            for (int i = 0; i < 15; i++) {
                questionString = choseQuestion(activePlayer);
                activePlayer = allBuzz();
                answerQuestion(questionString, activePlayer);

            }
            // for i 1-15 {
            //   en spiller vælger kategori i sin tråd
            //       spørgsmålk dskrives til alle sockets
            //   alle buzz(); i hver deres tråd
            //  ham der buzzede svarere spørgsmål
            //     resultat skrives til alle sockets

        }


    }

    private void answerQuestion(String chosenQuestion, int activePlayer) throws IOException {
        Scanner in = new Scanner(sockets.get(activePlayer).getInputStream());
        PrintWriter out = new PrintWriter(sockets.get(activePlayer).getOutputStream());


        int categoryNumber = (int) chosenQuestion.charAt(0) - (int) 'a';
        int questionNumber = Integer.parseInt(chosenQuestion.substring(1)) / 100 - 1;
        out.println(game.getQuestionText(categoryNumber, questionNumber));
        out.flush();
        String answer = fetchLine(in, out);
        String correctAnswer = game.answerQuestion(categoryNumber, questionNumber, answer);
        if (correctAnswer == null) {
            out.println("Correct, You got " + (questionNumber + 1) * 100 + " points");
        } else {
            out.println("Wrong, the answer was: " + correctAnswer);
        }
        out.flush();

    }


    private String choseQuestion(int activePlayer) throws IOException {
        Scanner in = new Scanner(sockets.get(activePlayer).getInputStream());
        PrintWriter out = new PrintWriter(sockets.get(activePlayer).getOutputStream());
        out.println("Chose question :");
        String q = fetchLine(in, out);

        return q;

    }


    public int allBuzz() throws IOException, InterruptedException {

        int activePlayer = 0;

        /*

     Scanner in = new Scanner(System.in);
        String str = "";

        Thread t1 = new Thread();
        Thread t2 = new Thread();
        Thread t3 = new Thread();
        t1.start();
        System.out.println(t1);
        t2.start();
        System.out.println(t2);
        t3.start();
        System.out.println(t3);


        for (Player p : game.getPlayers()) {
            Thread thread = new Thread(();

            thread.start();
            System.out.println("Start " + thread);

            System.out.println("Buzz in");

            if (in.hasNext()) {
                System.out.println(thread);
                break;
            }

     */
        return activePlayer;

    }


    private void getPlayers(int numberOfPlayers) throws IOException, InterruptedException {
        //tråde der connecter og henter navne
        for (int i = 0; i < numberOfPlayers; i++) {
            Socket socket = serverSocket.accept();
            sockets.add(socket);
            System.out.println(socket.getRemoteSocketAddress()+" connected");
            //add player
            // lav tråd, gem den i liste og kør den
            NewPlayer newPlayer=new NewPlayer(i,new Scanner(socket.getInputStream()), new PrintWriter(socket.getOutputStream()));
            Thread thread=new Thread(newPlayer);
            threads.add(thread);
            thread.start();
        }
        //vent på alle har indtatste navn
        for (int i = 0; i < numberOfPlayers; i++) {
            threads.get(i).join();
        }


    }
    private String fetchLine(Scanner in,PrintWriter out) {
        out.print("> ");
        out.flush();
        String line = in.nextLine().strip();
        return line;
    }
}
