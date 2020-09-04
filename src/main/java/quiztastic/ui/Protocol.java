package quiztastic.ui;

import quiztastic.app.Quiztastic;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Scanner;

public class Protocol {
    private final Quiztastic quiz;
    private final Scanner in;
    private final PrintWriter out;

    public Protocol(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.quiz = Quiztastic.getInstance();
    }

    private String fetchCommand () {
        out.print("> ");
        out.flush();
        String line = in.nextLine().strip().toLowerCase();
        return line;
    }
<<<<<<< HEAD
    private void displayhelp(){
        out.println("Your options are:");
        out.println("- [h]elp: ask for help");
        out.println("- [d]raw: draw the board");
        out.println("- [a]nswer A200: get the question for category A, question 200.");
    }

    public void run () {
        out.println("Welcome to Quiztastic !");
        displayhelp();
=======

    public void run () {
        out.println("Welcome to Quiztastic!");
        out.println("Your options are:");
        out.println("- [h]Help: ask for help");
        out.println("- [d]Draw: draw the quiz board");
>>>>>>> 2e3d0e35b76556f1fc8967e4f92bb4603782138c
        String line = fetchCommand();
        while (!line.equals("quit")) {
            String[] arrOfStr = line.split(" ");
            switch (arrOfStr[0]) {
                case "h":
                case "help":
                    displayhelp();
                   break;
                case "d":
                case "draw":
                    System.out.println(this.quiz.getBoard());
                    break;
                case "a":
                case "answer":
                    System.out.println(this.quiz.getBoard().getQuestionFromString(arrOfStr[1]));
                    break;
                default:
                   out.println("Unknown command! " + line);

            }
            out.flush();
            line = fetchCommand();
        }

    }

}
