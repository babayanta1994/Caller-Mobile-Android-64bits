package ru.true_ip.trueip.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ru.true_ip.trueip.models.responses.UserModel;


/**
 * Created by user on 26-Sep-17.
 */

@Entity(tableName = "Users")
public class UserDb {

    @PrimaryKey(autoGenerate = true)
    public int user_id;

    @ColumnInfo(name = "token")
    public String token;

    @ColumnInfo(name = "token_cloud")
    public String token_cloud;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "email")
    public String email;

    public UserDb() {

    }

    public UserDb(UserModel userModel) {
        user_id = userModel.getId();
        token = userModel.getApi_token();
        name = userModel.getName();
        email = userModel.getEmail();
    }

    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_cloud() {
        return token_cloud;
    }
    public void setToken_cloud(String token_cloud) {
        this.token_cloud = token_cloud;
    }

    public String getName() {return name;}
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
