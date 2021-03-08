package ru.true_ip.trueip.models.responses;

/**
 *
 * Created by Andrey Filimonov on 21.12.2017.
 */

public class Device {
    private String locale;
    private String push_token;

    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }

    public String getPush_token() { return push_token; }
    public void setPush_token(String push_token) { this.push_token = push_token; }
}
