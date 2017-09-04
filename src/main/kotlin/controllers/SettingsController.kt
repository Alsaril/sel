package controllers

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.auth.AuthRequest
import models.auth.Token
import network.AuthRetrofitClient
import retrofit2.Response
import utils.CloseListener
import utils.Dialogs
import utils.FileHelper
import utils.awaitResponse


class SettingsController : LoadController<Boolean>() {
    private var authApi = AuthRetrofitClient.apiService

    @FXML private lateinit var login: TextField
    @FXML private lateinit var password: TextField

    fun save(actionEvent: ActionEvent) = launch(JavaFx) {
        val call = authApi.auth(AuthRequest(login.text, password.text))
        val response: Response<Token>
        try {
            response = call.awaitResponse()
        } catch (t: Throwable) {
            Dialogs.showErrorDialog("Не удалось получить токен!")
            return@launch
        }
        val body = response.body()
        if (response.code() == 200 && body != null) {
            FileHelper.saveToken(body.token)
            Dialogs.showDialog("Токен получен!")
            close(true)
        } else {
            Dialogs.showErrorDialog("Не удалось получить токен!")
        }
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>? = null) {
            LoadController.show(owner, callback,
                    path = "/view/SettingsView.fxml",
                    title = "Настройки",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
