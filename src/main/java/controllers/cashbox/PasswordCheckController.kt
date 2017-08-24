package controllers.cashbox

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.PasswordField
import javafx.stage.Modality
import javafx.stage.Stage
import utils.Dialogs

class PasswordCheckController {
    @FXML private lateinit var passField: PasswordField

    var isOk = false
        private set

    fun okHandle() {
        if (passField.text == "0000") {
            isOk = true
            close()
        } else {
            Dialogs.showErrorDialog("Неверный пароль")
        }
    }

    private fun close() {
        val stage = passField.scene.window as Stage
        stage.close()
    }

    companion object {
        fun show(owner: Node, callback: OnCloseListener) {
            val stage = Stage()
            val loader = FXMLLoader()
            loader.location = javaClass.getResource("/view/cashbox/PasswordCheck.fxml")
            val categoryAddFXML = loader.load<Parent>()
            stage.title = "Введите пароль"
            stage.isResizable = false
            stage.scene = Scene(categoryAddFXML)
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner(owner.scene.window)

            val controller = loader.getController<PasswordCheckController>()
            stage.showAndWait()

            callback.onClose(controller.isOk)
        }
    }
}

interface OnCloseListener {
    fun onClose(param: Boolean)
}
