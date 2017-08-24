package controllers

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.stage.Modality
import models.auth.AuthRequest
import models.auth.Token
import network.AuthApi
import network.AuthRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.CloseListener
import utils.Dialogs
import utils.FileHelper


/**
 * Created by andrey on 25.07.17.
 */
class SettingsController: LoadController<Boolean>(){
    @FXML private lateinit var login: TextField
    @FXML private lateinit var password: TextField

    fun save(actionEvent: ActionEvent) {
        val call = retrofitApi.auth(AuthRequest(login.text, password.text))
        call.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.code() == 200) {
                    FileHelper.saveToken(response.body()?.token)
                    Dialogs.showDialog("Токен получен!")
                    Platform.runLater { close(true) }
                } else {
                    Dialogs.showErrorDialog("Не удалось получить токен!")
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Dialogs.showExeptionDialog(t.message.toString())
            }
        })
    }

    internal var retrofitApi = AuthRetrofitClient.getApiService()

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/SettingsView.fxml",
                    title = "Настройки",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
