package controllers

import api.Result
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

    private var id: Int? = null

    @FXML
    private fun initialize() {

    }

    fun edit(client: Client) {
        id = client.id
        name.text = client.name
        phone.text = client.phone
        address.text = client.address
        comment.text = client.comment
    }

    fun ok() = launch(JavaFx) {
        if (name.text.isNullOrBlank() || phone.text.isNullOrBlank() || address.text.isNullOrBlank()) {
            Dialogs.showErrorDialog("Пустые поля")
            return@launch
        }

        val id = this@NewClientController.id

        val client = Client(id ?: 0, name.text, phone.text, address.text, comment.text)

        val result: Result<Void> =
                if (id == null) {
                    api.addClient(client)
                } else {
                    api.editClient(id.toString(), client)
                }.await()

        if (result.isSuccessful()) {
            close(true)
        } else {
            Dialogs.showErrorDialog(result.error)
        }
    }

    companion object {
        fun show(owner: Window, client: Client? = null, callback: CloseListener<Boolean>) {
            LoadController.show<Boolean, NewClientController>(owner, callback,
                    path = "/view/reserves/NewClient.fxml",
                    title = "Добавить клиента",
                    minHeight = 600.0,
                    minWidth = 800.0,
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL) {
                client?.let {
                    edit(client)
                }
            }
        }
    }
}