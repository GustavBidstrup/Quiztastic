package quiztastic.core;

import java.util.ArrayList;
import java.util.List;

/** A Jeopardy Board
 *
 */
public class Board {
    private final List<Group> groups;

    public Board(List<Group> groups) {
        this.groups = List.copyOf(groups);
        if (this.groups.size() != 6) {
            throw new IllegalArgumentException(
                    "Should be 6 groups, there were " + groups.size());
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Group getGroupfromChar(String choice){
        char c= choice.charAt(0);
        return this.groups.get((int)c-(int) ('a'));
    }

    public Question getQuestionFromString(String choice){
        Group group=this.getGroupfromChar(choice);
        choice=choice.substring(1);
        int value=Integer.parseInt(choice);
        Question question=group.getQuestion(value);
        return question;
    }

   
    @Override
    public String toString() {
        String retVal="";

        char c='A';
        for (Group group:groups){
            retVal=retVal+c+": "+String.format("%-27s",group.category.getName());
            c++;
        }

        retVal=retVal+"\n";
        for (int i=0;i<5;i++){
            for (Group group:groups){
                retVal=retVal+String.format("%-30d",group.questions.get(i).getScore());
            }
            retVal=retVal+"\n";

        }


        return retVal;
    }

    public static class Group {
        private final Category category;
        private final List<Question> questions;

        public Group(Category category, List<Question> questions) {
            this.category = category;
            this.questions = List.copyOf(questions);
            validate();
        }

        private void validate() {
            if (questions.size() != 5) {
                throw new IllegalArgumentException(
                        "Should be 5 groups, there were " + questions.size());
            }
            for (Question q : questions) {
                if (!q.getCategory().equals(category)) {
                    throw new IllegalArgumentException("Expected all categories to be "
                            + category + " but was " + q.getCategory());
                }
            }
        }

        public Category getCategory() {
            return category;
        }

        public Question getQuestion(int value){
            for (Question question:this.questions){
                if (question.getScore()==value){
                    return question;
                }
            }
            return null;
        }

        public List<Question> getQuestions() {
            return questions;
        }

        @Override
        public String toString() {
            return "Group{" +
                    "category=" + category +
                    ", questions=" + questions +
                    '}';
        }
    }
}
