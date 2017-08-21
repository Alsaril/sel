package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.auth.AuthRequest;
import models.auth.Token;
import network.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Dialogs;
import utils.FileHelper;


/**
 * Created by andrey on 25.07.17.
 */
public class SettingsController {

    @FXML
    private TextField login;

    @FXML
    private TextField password;


    static AuthApi api = AuthRetrofitClient.getApiService();

    public void save(ActionEvent actionEvent) {
        Call<Token> call = api.auth(new AuthRequest(login.getText(), password.getText()));
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 200) {
                    FileHelper.saveToken(response.body().getToken());
                } else {
                    Dialogs.showErrorDialog("Не удалось получить токен!");
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });
    }
}
