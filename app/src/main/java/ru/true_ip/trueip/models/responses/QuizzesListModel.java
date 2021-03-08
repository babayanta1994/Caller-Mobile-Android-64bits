package ru.true_ip.trueip.models.responses;

import java.util.List;

/**
 * Created by Eugen on 06.09.2017.
 */

public class QuizzesListModel {

    List<QuizzModel> quizzes;

    public List<QuizzModel> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<QuizzModel> quizzes) {
        this.quizzes = quizzes;
    }
}
