package models.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by andrey on 05.08.17.
 */
public class AuthRequest {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;

    public AuthRequest(String login, String password) {
        this.username = login;
        this.password = password;
    }

    public String getLogin() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.username = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
