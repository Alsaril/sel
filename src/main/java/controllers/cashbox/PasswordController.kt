package controllers.cashbox

import controllers.LoadController
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.PasswordField
import javafx.stage.Modality
import utils.CloseListener
import utils.Dialogs

class PasswordController : LoadController<Boolean>() {
    @FXML private lateinit var passField: PasswordField

    fun okHandle() {
        if (passField.text == "0000") {
            close(true)
        } else {
            Dialogs.showErrorDialog("Неверный пароль")
        }
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/cashbox/PasswordCheck.fxml",
                    title = "Введите пароль",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}