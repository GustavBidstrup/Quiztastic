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
import java.net.SocketAddress;
import java.util.*;

public class Server {
    private final int port = 6060;
    private final ServerSocket serverSocket = new ServerSocket(port);

    private List<Client> clients = Collections.synchronizedList(new ArrayList<Client>());
    private RunGame runGame = new RunGame();
    private Game game;
    private List<Thread> threads = new ArrayList<>();
    public static volatile Client playerToAnswer = null;
    public static volatile boolean someoneBuzzed = false;
   // public static volatile String filename = "master_season1-35clean.tsv";
     public static volatile String filename = "DanskeSpørgsmålQuiztastic.tsv";

    private int numberOfPlayers = 3;

    public void setPlayerToAnswer(Client client) {
        this.playerToAnswer = client;
    }

    public Server() throws IOException, InterruptedException {

        while (true) {
            getPlayers(numberOfPlayers);
            game = Quiztastic.getInstance(filename).getCurrentGame();
            //starter 3 tråde med buzz returneer nr på første spiller
            int activePlayerNumber=0;
            Client activePlayer = clients.get(activePlayerNumber);
            String questionString;

            for (int i = 0; i < 3; i++) {
                drawBoardToAll();

                questionString = choseQuestion(activePlayer);

                writeToAll(activePlayer.getPlayer().getName() + " Chose question:");
                int categoryNumber = (int) questionString.charAt(0) - (int) 'a';

                int questionNumber = Integer.parseInt(questionString.substring(1)) / 100 - 1;
                writeToAll(game.getQuestionText(categoryNumber, questionNumber));
                allBuzz();
                writeToAll(playerToAnswer.getPlayer().getName() + " buzzed first and has to answer.");

                answerQuestion(questionString, playerToAnswer);
                activePlayerNumber = (activePlayerNumber + 1) % clients.size();
                activePlayer=clients.get(activePlayerNumber);


            }
            writeResult();

            for (Client c :clients ) {
                c.getSocket().close();
            }

            clients.clear();

        }

    }

    private void writeResult() throws IOException {
        HashMap<String, Integer> map = new HashMap();
        for (int i = 0; i < clients.size(); i++) {
            String str = clients.get(i).getPlayer().getName();
            int tal = clients.get(i).getScore();
            map.put(str, tal);
        }
        int maxValue = ((int) Collections.max(map.values()));
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == maxValue) {
                writeToAll(entry.getKey() + " won" + " with" + maxValue + " points!");
            }
        }
    }


    private void answerQuestion(String chosenQuestion, Client playerToAnswer) throws IOException {

        emptyAllInStreams();
        int categoryNumber = (int) chosenQuestion.charAt(0) - (int) 'a';
        int questionNumber = Integer.parseInt(chosenQuestion.substring(1)) / 100 - 1;
        // writeToAll(game.getQuestionText(categoryNumber, questionNumber));
        // activePlayer.out.flush();
        String answer = fetchLine(playerToAnswer.getIn(), playerToAnswer.getOut());
        writeToAll(playerToAnswer.getPlayer().getName() + " answered:" + answer);

        String correctAnswer = game.answerQuestion(categoryNumber, questionNumber, answer);
        if (correctAnswer == null) {
            writeToAll(playerToAnswer.getPlayer().getName() +" answered correct and got "+ (questionNumber + 1) * 100 + " points");
            playerToAnswer.setScore(playerToAnswer.getScore() + (questionNumber + 1) * 100);
        } else {
            writeToAll("Wrong, the answer was: " + correctAnswer+" ,"+playerToAnswer.getPlayer().getName()+" lost "+(questionNumber + 1) * 100 + " points");
            playerToAnswer.setScore(playerToAnswer.getScore() - (questionNumber + 1) * 100);

        }


    }

    private String choseQuestion(Client playerToChose) throws IOException {
        emptyAllInStreams();
        System.out.println("chose");
        playerToChose.getOut().println("Chose question :");
        playerToChose.getOut().flush();

        String q = fetchLine(playerToChose.getIn(), playerToChose.getOut());
        int categoryNumber = -1;
        int questionNumber = -1;
        while (categoryNumber == -1) {
            System.out.println(q);
            try {
                categoryNumber = (int) q.charAt(0) - (int) 'a';
                questionNumber = Integer.parseInt(q.substring(1)) / 100 - 1;
                if (game.isAnswered(categoryNumber, questionNumber)) {
                    categoryNumber = -1;
                    System.out.println("isanswered");
                }
                if (categoryNumber < 0 || categoryNumber > 5) {
                    categoryNumber = -1;
                    System.out.println("illegal category");
                }
                if (questionNumber < 0 || questionNumber > 4) {
                    categoryNumber = -1;
                    System.out.println("illegal questio");
                }
            } catch (Exception e) {
                categoryNumber = -1;
                System.out.println("illegal: "+q);
            }
            if (categoryNumber == -1) {
                q = fetchLine(playerToChose.getIn(), playerToChose.getOut());
                System.out.println("illegal");
            }
        }

        ;
        writeToAll(playerToChose.getPlayer().getName()+" chose "+q);
        return q;

    }

    private void allBuzz() throws IOException, InterruptedException {
        emptyAllInStreams();
        Buzz buzz0 = new Buzz(clients.get(0));
        Buzz buzz1 = new Buzz(clients.get(1));
        Buzz buzz2 = new Buzz(clients.get(2));


        Thread thread0 = new Thread(buzz0);
        Thread thread1 = new Thread(buzz1);
        Thread thread2 = new Thread(buzz2);
        writeToAll("All Buzz !");

        thread0.start();
        thread1.start();
        thread2.start();


        thread0.join();
        thread1.join();
        thread2.join();

        someoneBuzzed=false;
        }


        private void emptyAllInStreams() throws IOException {
           /* for (Client client:clients) {
                client.setIn(new Scanner(client.getSocket().getInputStream()));
            }*/
        }

        private void getPlayers ( int numberOfPlayers) throws IOException, InterruptedException {
            //tråde der connecter og henter navne
            Client client;
            for (int i = 0; i < numberOfPlayers; i++) {
                Socket socket = serverSocket.accept();
                client = new Client(socket);
                clients.add(client);
                System.out.println(socket.getRemoteSocketAddress() + " connected");
                //add player
                // lav tråd, gem den i liste og kør den
                NewPlayer newPlayer = new NewPlayer(client);
                Thread thread = new Thread(newPlayer);
                threads.add(thread);
                thread.start();
            }
            //vent på alle har indtatste navn
            for (int i = 0; i < numberOfPlayers; i++) {
                threads.get(i).join();
            }


        }
        private String fetchLine (Scanner in, PrintWriter out){
            out.print("> ");
            out.flush();
            String line = in.nextLine();
            return line;
        }

        private void drawBoardToAll () throws IOException {
            for (Client client : clients) {


                drawBoard(game.getBoard(), client.getOut());

            }
        }
        private void writeToAll (String msg) throws IOException {
            for (Client client : clients) {
                client.getOut().println(msg);
                client.getOut().flush();
            }
        }

        private void drawBoard (Board board, PrintWriter out){
            String fiveSpaces = "     ";
            String str = "\n";
            for (Client client:clients) {
                str = str + String.format("%-13s", client.getPlayer().getName() + " " + client.getScore());
            }
            str = str + "\n";
            str = str + "\n";

            char c = 'A';
            for (int i = 0; i < 6; i++) {
                str = str + "|" + fiveSpaces + " " + c + " " + fiveSpaces;
                c++;
            }
            str = str + "\n";

            for (Board.Group group : board.getGroups()) {
                str = str + "|" + String.format("%-13s", group.getCategory().getName());

            }
            str = str + "|\n";

            for (int questionNumber = 0; questionNumber < 5; questionNumber++) {
                for (int groupNumber = 0; groupNumber < 6; groupNumber++) {
                    if (game.isAnswered(groupNumber, questionNumber)) {
                        str = str + "|" + fiveSpaces + "---" + fiveSpaces;
                    } else {
                        str = str + "|" + fiveSpaces + ((questionNumber + 1) * 100) + fiveSpaces;

                    }
                }
                str = str + "|\n";

            }
            out.println(str);
        }
    }

