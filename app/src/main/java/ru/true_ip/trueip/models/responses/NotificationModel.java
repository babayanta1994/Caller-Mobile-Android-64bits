package ru.true_ip.trueip.models.responses;

/**
 * Created by Eugen on 07.09.2017.
 */

public class NotificationModel {

    /**
     * id : 79
     * theme : Repudiandae iste quia necessitatibus ipsam ad ut quidem.
     * text : So she set the little door about fifteen inches high: she tried the little golden key, and when she first saw the Mock Turtle. 'Certainly not!' said Alice very meekly: 'I'm growing.' 'You've no.
     * actual_to_date : 1994-02-20 14:06:53
     * is_viewed : false
     */

    private int id;
    private String theme;
    private String text;
    private String actual_from_date;
    private String actual_to_date;
    private int is_viewed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getActual_to_date() {
        return actual_to_date;
    }

    public void setActual_to_date(String actual_to_date) {
        this.actual_to_date = actual_to_date;
    }

    public String getActual_from_date() {
        return actual_from_date;
    }

    public void setActual_from_date(String actual_from_date) {
        this.actual_from_date = actual_from_date;
    }

    public int isIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(int is_viewed) {
        this.is_viewed = is_viewed;
    }
}
