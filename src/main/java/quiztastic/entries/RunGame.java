package quiztastic.entries;

import quiztastic.app.Quiztastic;
import quiztastic.ui.Server;

import java.io.PrintWriter;
import java.util.Scanner;

public class RunGame {
    private final Quiztastic quiz;


    public RunGame() {
        this.quiz = Quiztastic.getInstance(Server.filename);
    }


}
