package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Player;
import quiztastic.core.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private final Board board;
    private final List<Answer> answerList;
    private volatile List<Player> players=new ArrayList<>();
    private volatile Player playerToChoseCategori;

    public Game(Board board, List<Answer> answerList) {
        this.board = board;
        this.answerList = answerList;
    }

    public void addPlayer(Player player){
        players.add(player);
    }
    public int numberOffPlayers(){
        return players.size();
    }
    public List<Category> getCategories() {
        List<Category> list = new ArrayList<>();
        for (Board.Group group : this.board.getGroups()) {
            Category category = group.getCategory();
            list.add(category);
        }
        return list;
    }

    public String answerQuestion(int categoryNumber, int questionNumber, String answer) {
        Question q = getQuestion(categoryNumber, questionNumber);
        answerList.add(new Answer(categoryNumber, questionNumber, answer));
        if (q.getAnswer().toLowerCase().equals(answer.toLowerCase())) {
            return null;
        } else {
            return q.getAnswer();
        }
    }

    public String getQuestionText(int categoryNumber, int questionNumber) {
        return getQuestion(categoryNumber, questionNumber).getQuestion();
    }

    private Question getQuestion(int categoryNumber, int questionNumber) {
        return this.board.getGroups().get(categoryNumber).getQuestions().get(questionNumber);
    }

    public boolean isAnswered(int categoryNumber, int questionNumber) {
        for (Answer a : answerList) {
            if (a.hasIndex(categoryNumber, questionNumber)) {
                return true;
            }
        }
        return false;
    }

    public Board getBoard() {
        return board;
    }

    private class Answer {
        private final int categoryNumber;
        private final int questionNumber;
        private final String answer;

        private Answer(int categoryNumber, int questionNumber, String answer) {
            this.categoryNumber = categoryNumber;
            this.questionNumber = questionNumber;
            this.answer = answer;
        }

        public boolean hasIndex(int categoryNumber, int questionNumber)  {
            return this.categoryNumber == categoryNumber && this.questionNumber == questionNumber;
        }


    }
}
