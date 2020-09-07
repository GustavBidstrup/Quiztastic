package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Question;

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
    private void displayhelp(){
        out.println("Your options are:");
        out.println("- [h]elp: ask for help");
        out.println("- [d]raw: draw the board");
        out.println("- [a]nswer A200: get the question for category A, question 200.");
        out.println("- [q]uit:");
    }

    public String answerQuestion(Question question) {
        String str = "";

        System.out.println("Answer the question?");
        String answer = fetchCommand();
        if (answer.equals(question.getAnswer().toLowerCase())) {
            str = "Correct";
        } else {
            str = "incorrect";
        }
        return str;
    }

    public void run () {
        out.println("Welcome to Quiztastic!");
        displayhelp();
        String answer;
        Question question;

        String line = fetchCommand();
        while (!(line.equals("quit")||line.equals("q"))) {
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
                    question = this.quiz.getBoard().getQuestionFromString(arrOfStr[1]);
                    System.out.println(question.getQuestion());
                    System.out.println(answerQuestion(question));
                    break;

                default:
                   out.println("Unknown command! " + line);

            }
            out.flush();
            line = fetchCommand();
        }

    }




}
