package controllers

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.stage.Modality
import javafx.stage.Window
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.reserve.Client
import utils.CloseListener
import utils.Dialogs

class NewClientController : LoadController<Boolean>() {
    @FXML private lateinit var name: TextField
    @FXML private lateinit var phone: TextField
    @FXML private lateinit var address: TextField
    @FXML private lateinit var comment: TextArea

    @FXML
    private fun initialize() {

    }

    fun ok() = launch(JavaFx) {
        if (name.text.isNullOrBlank() || phone.text.isNullOrBlank() || address.text.isNullOrBlank()) {
            Dialogs.showErrorDialog("Пустые поля")
            return@launch
        }

        val client = Client(0, name.text, phone.text, address.text, comment.text)

        val result = api.addClient(client).await()

        if (result.isSuccessful()) {
            close(true)
        } else {
            Dialogs.showErrorDialog(result.error)
        }
    }

    companion object {
        fun show(owner: Window, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/reserves/NewClient.fxml",
                    title = "Добавить клиента",
                    minHeight = 600.0,
                    minWidth = 800.0,
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}