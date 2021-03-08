package ru.true_ip.trueip.models.requests;


import java.util.ArrayList;

/**
 * Created by user on 15-Sep-17.
 */

public class QuizzesAnswers {


    private ArrayList<Answer> answers;
    private String free_answer;

    public QuizzesAnswers() {
        this.answers = new ArrayList<>();
        this.free_answer = "";
    }

    public QuizzesAnswers(ArrayList<Answer> answers, String free_answer) {
        this.answers = answers;
        this.free_answer = free_answer;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public String getFree_answer() { return free_answer; }
    public void setFree_answer(String free_answer) { this.free_answer = free_answer; }

    public static class Answer {
        /**
         * id : 321
         * text : Текст варианта ответа с ID=321.
         */

        private int id;
        private String text;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
