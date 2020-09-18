package quiztastic.entries;

import quiztastic.app.Quiztastic;
import quiztastic.ui.Server;

public class DisplayBoard {

    public static void main(String[] args) {
        Quiztastic quiz = Quiztastic.getInstance(Server.filename);

        System.out.println(quiz.getBoard());

    }
}
