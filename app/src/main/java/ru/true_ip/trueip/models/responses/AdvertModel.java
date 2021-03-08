package ru.true_ip.trueip.models.responses;

/**
 * Created by Eugen on 06.09.2017.
 *
 */

public class AdvertModel {

    /**
     * id : 879
     * theme : Voluptatem eaque dicta quia repudiandae culpa voluptas et.
     * text : Caterpillar. Here was another puzzling question; and as it went, 'One side will make you a present of everything I've said as yet.' 'A cheap sort of lullaby to it in with the end of the tale was.
     * actual_from_date : 1983-05-08 16:03:19
     * actual_to_date : 1977-08-15 08:04:31
     * is_viewed : true
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

    public String getActual_from_date() {
        return actual_from_date;
    }

    public void setActual_from_date(String actual_from_date) {
        this.actual_from_date = actual_from_date;
    }

    public String getActual_to_date() {
        return actual_to_date;
    }

    public void setActual_to_date(String actual_to_date) {
        this.actual_to_date = actual_to_date;
    }

    public int isIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(int is_viewed) {
        this.is_viewed = is_viewed;
    }
}
