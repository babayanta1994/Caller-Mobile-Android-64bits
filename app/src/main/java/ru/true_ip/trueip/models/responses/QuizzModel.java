package ru.true_ip.trueip.models.responses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eugen on 07.09.2017.
 */

public class QuizzModel implements Parcelable {

    /**
     * id : 738
     * question : In excepturi facilis doloremque debitis aut sint consectetur.
     * answers : [{"id":403,"text":"Iure voluptas tempora dolorem quo et voluptatum."}]
     * selected_answers : [403]
     * is_free_answer : true
     * free_answer_title : Aliquid est aliquam ut consequuntur nesciunt aut.
     * is_multiple : false
     * allow_view_results : true
     * start_date : 1984-07-14 13:13:58
     * actual_to_date : 2015-08-17 03:38:37
     * is_viewed : true
     */

    private int id;
    private String title;
    private String question;
    private int free_answer;
    private String free_answer_title;
    private int multiple;
    private int allow_view_results;
    private String start_at;
    private String end_at;
    private int is_viewed;
    private int is_private;
    private List<AnswerModel> answers;
    private List<AnswerModel> selected_answer;
    private FreeAnswer selected_free;

    protected QuizzModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        question = in.readString();
        free_answer = in.readInt();
        free_answer_title = in.readString();
        multiple = in.readInt();
        allow_view_results = in.readInt();
        start_at = in.readString();
        end_at = in.readString();
        is_viewed = in.readInt();
        is_private = in.readInt();
        answers = in.createTypedArrayList(AnswerModel.CREATOR);
        selected_answer = in.createTypedArrayList(AnswerModel.CREATOR);
        selected_free = in.readParcelable(FreeAnswer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(question);
        dest.writeInt(free_answer);
        dest.writeString(free_answer_title);
        dest.writeInt(multiple);
        dest.writeInt(allow_view_results);
        dest.writeString(start_at);
        dest.writeString(end_at);
        dest.writeInt(is_viewed);
        dest.writeInt(is_private);
        dest.writeTypedList(answers);
        dest.writeTypedList(selected_answer);
        dest.writeParcelable(selected_free, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizzModel> CREATOR = new Creator<QuizzModel>() {
        @Override
        public QuizzModel createFromParcel(Parcel in) {
            return new QuizzModel(in);
        }

        @Override
        public QuizzModel[] newArray(int size) {
            return new QuizzModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int isIs_private() { return is_private; }

    public void setIs_private(int is_private) { this.is_private = is_private; }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int isIs_free_answer() {
        return free_answer;
    }

    public void setIs_free_answer(int is_free_answer) {
        this.free_answer = is_free_answer;
    }

    public String getFree_answer_title() {
        return free_answer_title;
    }

    public void setFree_answer_title(String free_answer_title) {
        this.free_answer_title = free_answer_title;
    }

    public int isIs_multiple() {
        return multiple;
    }

    public void setIs_multiple(int is_multiple) {
        this.multiple = is_multiple;
    }

    public int isAllow_view_results() {
        return allow_view_results;
    }

    public void setAllow_view_results(int allow_view_results) {
        this.allow_view_results = allow_view_results;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_date) {
        this.start_at = start_date;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String actual_to_date) {
        this.end_at = actual_to_date;
    }

    public int isIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(int is_viewed) {
        this.is_viewed = is_viewed;
    }

    public List<AnswerModel> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerModel> answers) {
        this.answers = answers;
    }

    public List<AnswerModel> getSelected_answer() {
        return selected_answer;
    }

    public void setSelected_answer(List<AnswerModel> selected_answer) {
        this.selected_answer = selected_answer;
    }

    public FreeAnswer getSelected_free() {
        return selected_free;
    }

    public void setSelected_free(FreeAnswer selected_free) {
        this.selected_free = selected_free;
    }

    public static class AnswerModel implements Parcelable {
        /**
         * id : 403
         * text : Iure voluptas tempora dolorem quo et voluptatum.
         */

        private int id;
        private String text;
        private boolean isSelected;

        public AnswerModel() {}

        protected AnswerModel(Parcel in) {
            id = in.readInt();
            text = in.readString();
            isSelected = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(text);
            dest.writeByte((byte) (isSelected ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<AnswerModel> CREATOR = new Creator<AnswerModel>() {
            @Override
            public AnswerModel createFromParcel(Parcel in) {
                return new AnswerModel(in);
            }

            @Override
            public AnswerModel[] newArray(int size) {
                return new AnswerModel[size];
            }
        };

        public boolean isSelected() { return isSelected; }

        public void setSelected(boolean isSelected) { this.isSelected = isSelected; }

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

    public static class FreeAnswer implements Parcelable {
        private String free_answer_value;

        protected FreeAnswer(Parcel in) {
            free_answer_value = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(free_answer_value);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<FreeAnswer> CREATOR = new Creator<FreeAnswer>() {
            @Override
            public FreeAnswer createFromParcel(Parcel in) {
                return new FreeAnswer(in);
            }

            @Override
            public FreeAnswer[] newArray(int size) {
                return new FreeAnswer[size];
            }
        };

        public String getFree_answer_value() { return free_answer_value; }

        public void setFree_answer_value(String free_answer_value) {
            this.free_answer_value = free_answer_value;
        }
    }
}
