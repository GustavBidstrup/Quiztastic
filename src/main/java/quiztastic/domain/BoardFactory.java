package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BoardFactory {
    public final QuestionRepository questionRepository;

    public BoardFactory(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Board.Group makeGroup(Category c) throws IllegalArgumentException {
        List<Question> questions =
                questionRepository.getQuestionsWithCategory(c);
        if (questions.size() >= 5) {
            return new Board.Group(c, questions.subList(0, 5));
        } else {
            throw new IllegalArgumentException("Not enough questions in category");
        }
    }

    public Board makeBoard() {
            List<Board.Group> groups = new ArrayList<>();
            Category c;
            Random random=new Random();
            int categoryNumber=0;
            while(groups.size() < 6) {
                categoryNumber = random.nextInt(questionRepository.getCategories().size());
                c=questionRepository.getCategories().get(categoryNumber);

                try {
                    groups.add(makeGroup(c));
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }


        return new Board(groups);
    }
}
