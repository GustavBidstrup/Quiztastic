package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
import quiztastic.core.Question;
import quiztastic.entries.DisplayBoard;

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
        String line = in.nextLine().strip();
        return line;
    }
    private void displayhelp(){
        out.println("Your options are:");
        out.println("- [h]elp: ask for help");
        out.println("- [d]raw: draw the board");
        out.println("- [a]nswer A200: get the question for category A, question 200.");
        out.println("- [q]uit:");
    }
    private void drawBoard(Board board){
        String str="";

        char c='A';
        for (Board.Group group: board.getGroups()){
            str=str+c+": "+String.format("%-27s",group.getCategory().getName());
            c++;
        }

        str=str+"\n";
        for (int questionNumber=0;questionNumber<5;questionNumber++){
            for (int groupNumber=0;groupNumber<6;groupNumber++){
                if (quiz.getCurrentGame().isAnswered(groupNumber,questionNumber)){
                    str=str+String.format("%-30s","---");
                }else {
                    str = str + String.format("%-30d", (questionNumber + 1) * 100);
                }// str=str+String.format("%-30d",board.getGroups().get(groupNumber).getQuestions().get(questionNumber).getScore());
            }
            str=str+"\n";

        }
        out.println(str);
    }

    public void answerQuestion(String chosenQuestion) {
        int grupNumber=(int)chosenQuestion.charAt(0)-(int)'a';
        int questionNumber=Integer.parseInt(chosenQuestion.substring(1))/100-1;
        out.println(quiz.getCurrentGame().getQuestionText(grupNumber,questionNumber));
        String answer = fetchCommand();
        String correctAnswer=quiz.getCurrentGame().answerQuestion(grupNumber,questionNumber,answer);
        if (correctAnswer==null){
            out.println("Correct, Yuo got "+(questionNumber+1)*100+" points");
        }
        else {
            out.println("Wrong, the answer was: "+correctAnswer);
        }


    }

    public void run () {
        out.println("Welcome to Quiztastic!");
        displayhelp();
        String answer;
        Question question;
        Board board=quiz.getBoard();
        String line = fetchCommand().toLowerCase();
        while (!(line.equals("quit")||line.equals("q"))) {
            String[] arrOfStr = line.split(" ");
            switch (arrOfStr[0]) {
                case "h":
                case "help":
                    displayhelp();
                   break;
                case "d":
                case "draw":
                    //System.out.println();
                    drawBoard(quiz.getBoard());
                    break;
                case "a":
                case "answer":
                    answerQuestion(arrOfStr[1]);
                   // question = quiz.getBoard().getQuestionFromString(arrOfStr[1]);
                    //System.out.println(question.getQuestion());
                    //System.out.println(answerQuestion(question));
                    break;

                default:
                   out.println("Unknown command! " + line);

            }
            out.flush();
            line = fetchCommand();
        }

    }




}
