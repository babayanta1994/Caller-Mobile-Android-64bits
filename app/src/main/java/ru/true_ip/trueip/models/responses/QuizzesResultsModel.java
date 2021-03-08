package ru.true_ip.trueip.models.responses;

import java.util.List;

/**
 * Created by user on 15-Sep-17.
 */

public class QuizzesResultsModel {


    /**
     * results : {"id":987,"question":"In recusandae nihil quis nihil iusto at occaecati temporibus.","answers":[{"text":"Corporis et consectetur fugiat architecto.","answers_count":33},{"text":"Omnis repudiandae necessitatibus voluptas voluptatem aut.","answers_count":34},{"text":"Totam dolores eligendi id fugit voluptate laborum.","answers_count":44},{"text":"Voluptate ut consectetur tempora id dolores minus.","answers_count":18},{"text":"Et voluptatem quis nisi eos.","answers_count":12}],"free_answers_count":30}
     */

    private QuizResult results;

    public QuizResult getResults() {
        return results;
    }

    public void setResults(QuizResult results) {
        this.results = results;
    }

    public static class QuizResult {
        /**
         * id : 987
         * question : In recusandae nihil quis nihil iusto at occaecati temporibus.
         * answers : [{"text":"Corporis et consectetur fugiat architecto.","answers_count":33},{"text":"Omnis repudiandae necessitatibus voluptas voluptatem aut.","answers_count":34},{"text":"Totam dolores eligendi id fugit voluptate laborum.","answers_count":44},{"text":"Voluptate ut consectetur tempora id dolores minus.","answers_count":18},{"text":"Et voluptatem quis nisi eos.","answers_count":12}]
         * free_answers_count : 30
         */

        private int id;
        private String question;
        private String title;
        private int is_private;
        private int results_free_count;
        private List<QuizAnswer> answers;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public int getResults_free_count() {
            return results_free_count;
        }

        public void setResults_free_count(int free_answers_count) {
            this.results_free_count = free_answers_count;
        }

        public String getTitle() { return title; }

        public void setTitle(String title) { this.title = title; }

        public int isIs_private() { return is_private; }

        public void setIs_private(int is_private) { this.is_private = is_private; }

        public List<QuizAnswer> getAnswers() {
            return answers;
        }

        public void setAnswers(List<QuizAnswer> answers) {
            this.answers = answers;
        }

        public static class QuizAnswer {
            /**
             * text : Corporis et consectetur fugiat architecto.
             * answers_count : 33
             */

            private int id;
            private String text;
            private int results_count;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getId() { return id; }

            public void setId(int id) { this.id = id; }

            public int getResults_count() {
                return results_count;
            }

            public void setResults_count(int answers_count) {
                this.results_count = answers_count;
            }
        }
    }
}
