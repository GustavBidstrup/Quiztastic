package quiztastic.ui;


import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
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
    private Game game;
    private List<Thread> threads = new ArrayList<>();
    public static volatile int  playerToAnswer=-1;
    public static volatile boolean someoneBuzzed=false;
    public static volatile String filename="master_season1-35clean.tsv";
    private int numberOfPlayers = 3;

    public void setPlayerToAnswer(int playerToAnswer) {
        this.playerToAnswer = playerToAnswer;
    }

    public Server() throws IOException, InterruptedException {
        while (true) {
            getPlayers(numberOfPlayers);
            game=  Quiztastic.getInstance(filename).getCurrentGame();
            System.out.println(game.getPlayers());
            //starter 3 tråde med buzz returneer nr på første spiller
            int activePlayer = 0;
            String questionString;

            for (int i = 0; i < 15; i++) {
                drawBoardToAll();
                questionString = choseQuestion(activePlayer);

                writeToAll(game.getPlayers().get(activePlayer).getName() + " Chose question:");
                int categoryNumber = (int) questionString.charAt(0) - (int) 'a';

                int questionNumber = Integer.parseInt(questionString.substring(1)) / 100 - 1;
                writeToAll(game.getQuestionText(categoryNumber, questionNumber));
                playerToAnswer = allBuzz();
                writeToAll(game.getPlayers().get(playerToAnswer).getName() + " buzzed first and has to answer.");

                System.out.println(playerToAnswer);
                answerQuestion(questionString, playerToAnswer);
                activePlayer = (activePlayer + 1) % numberOfPlayers;

            }


        }
        // for i 1-15 {
        //   en spiller vælger kategori i sin tråd
        //       spørgsmålk dskrives til alle sockets
        //   alle buzz(); i hver deres tråd
        //  ham der buzzede svarere spørgsmål
        //     resultat skrives til alle sockets

    }


    private void answerQuestion(String chosenQuestion, int activePlayer) throws IOException {


        Scanner in = new Scanner(sockets.get(activePlayer).getInputStream());
        PrintWriter out = new PrintWriter(sockets.get(activePlayer).getOutputStream());


        int categoryNumber = (int) chosenQuestion.charAt(0) - (int) 'a';
        int questionNumber = Integer.parseInt(chosenQuestion.substring(1)) / 100 - 1;
       // writeToAll(game.getQuestionText(categoryNumber, questionNumber));
        out.flush();
        String answer = fetchLine(in, out);
        writeToAll(game.getPlayers().get(activePlayer).getName() + " answered:" + answer);

        String correctAnswer = game.answerQuestion(categoryNumber, questionNumber, answer);
        if (correctAnswer == null) {
            writeToAll("Correct " + game.getPlayers().get(activePlayer).getName() + (questionNumber + 1) * 100 + " points");
            game.getPoints()[activePlayer] = game.getPoints()[activePlayer] + (questionNumber + 1) * 100;
            //  out.println("Correct, You got " + (questionNumber + 1) * 100 + " points");
        } else {
            writeToAll("Wrong, the answer was: " + correctAnswer);
            game.getPoints()[activePlayer] = game.getPoints()[activePlayer] - (questionNumber + 1) * 100;

        }
        out.flush();

    }

    private String choseQuestion(int activePlayer) throws IOException {
        Scanner in = new Scanner(sockets.get(activePlayer).getInputStream());
        PrintWriter out = new PrintWriter(sockets.get(activePlayer).getOutputStream());
        out.println("Chose question :");

        String q = fetchLine(in, out);
        int categoryNumber = -1;
        int questionNumber = -1;
        while (categoryNumber == -1) {
            try {
                categoryNumber = (int) q.charAt(0) - (int) 'a';
                questionNumber = Integer.parseInt(q.substring(1)) / 100 - 1;
                if (game.isAnswered(categoryNumber, questionNumber)) {
                    categoryNumber = -1;
                }
                if (categoryNumber < 0 || categoryNumber > 5) {
                    categoryNumber = -1;
                }
                if (questionNumber < 0 || questionNumber > 4) {
                    categoryNumber = -1;
                }
            } catch (Exception e) {
                categoryNumber = -1;
                questionNumber = -1;

            }
            if (categoryNumber == -1) {
                q = fetchLine(in, out);
            }
        }
        return q;

    }

    private int allBuzz() throws IOException, InterruptedException {
       /* Random random=new Random();
        return random.nextInt(3);*/
        Buzz buzz0=new Buzz(0,new Scanner(sockets.get(0).getInputStream()));
        Buzz buzz1=new Buzz(1,new Scanner(sockets.get(1).getInputStream()));
        Buzz buzz2=new Buzz(2,new Scanner(sockets.get(2).getInputStream()));



        Thread thread0=new Thread(buzz0);
        Thread thread1=new Thread(buzz1);
        Thread thread2=new Thread(buzz2);

        thread0.start();

        thread1.start();
        thread2.start();

        while (!someoneBuzzed){}
        thread0.stop();
        thread1.stop();
        thread2.stop();

        return playerToAnswer;
    }

/*
    public int allBuzz() throws IOException, InterruptedException {

        int activePlayer = 0;

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


        return activePlayer;

}

 */

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
    private void drawBoardToAll() throws IOException {
        for (int i = 0; i < numberOfPlayers ; i++) {
            PrintWriter out=new PrintWriter(sockets.get(i).getOutputStream());
            drawBoard(game.getBoard(),out);
            out.flush();

        }
    }
    private void writeToAll(String msg) throws IOException {
        for (int i = 0; i < numberOfPlayers; i++) {
            PrintWriter out = new PrintWriter(sockets.get(i).getOutputStream(), true);
            out.println(msg);

        }
    }

    private void drawBoard(Board board,PrintWriter out){
        String fiveSpaces="     ";
        String str="\n";
        for (int i = 0; i <3; i++) {
            str=str+String.format("%-13s",game.getPlayers().get(i).getName()+" "+ game.getPoints()[i]);
        }
        str=str+"\n";
        str=str+"\n";

        char c='A';
        for (int i = 0; i <6; i++) {
            str=str+"|"+fiveSpaces+" "+c+" "+fiveSpaces;
            c++;
        }
        str=str+"\n";

        for (Board.Group group: board.getGroups()){
            str=str+"|"+String.format("%-13s",group.getCategory().getName());

        }

        str=str+"|\n";
        for (int questionNumber=0;questionNumber<5;questionNumber++){
            for (int groupNumber=0;groupNumber<6;groupNumber++){
                if (game.isAnswered(groupNumber,questionNumber)){
                    str=str+"|"+fiveSpaces+"---"+fiveSpaces;
                }else {
                    str = str +"|"+ fiveSpaces+ ((questionNumber + 1) * 100)+fiveSpaces;

                }
            }
            str=str+"|\n";

        }
        out.println(str);
    }
}
