package utils

import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority

object Dialogs {
    fun showDialog(text: String) = Platform.runLater {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Уведомление"
        alert.headerText = null
        alert.contentText = text

        alert.showAndWait()
    }


    fun showErrorDialog(text: String) = Platform.runLater {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Ошибка"
        alert.headerText = null
        alert.contentText = text
        alert.showAndWait()

    }

    fun showExeptionDialog(exceptionText: String) = Platform.runLater {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Exception Dialog"

        val label = Label("The exception stacktrace was:")

        val textArea = TextArea(exceptionText)
        textArea.isEditable = false
        textArea.isWrapText = true

        textArea.maxWidth = java.lang.Double.MAX_VALUE
        textArea.maxHeight = java.lang.Double.MAX_VALUE
        GridPane.setVgrow(textArea, Priority.ALWAYS)
        GridPane.setHgrow(textArea, Priority.ALWAYS)

        val expContent = GridPane()
        expContent.maxWidth = java.lang.Double.MAX_VALUE
        expContent.add(label, 0, 0)
        expContent.add(textArea, 0, 1)

        alert.dialogPane.expandableContent = expContent

        alert.showAndWait()
    }
}

