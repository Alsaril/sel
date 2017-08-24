package controllers

import controllers.cashbox.PasswordController
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import utils.CloseListener

abstract class LoadController<T> {

    private lateinit var stage: Stage
    private lateinit var callback: CloseListener<T>

    public fun close(result: T) {
        stage.close()
        callback.invoke(result)
    }

    companion object {
        public fun <T, C : LoadController<T>> show(owner: Node, callback: CloseListener<T>,
                                                   path: String,
                                                   title: String = "",
                                                   minWidth: Double = 0.0,
                                                   minHeight: Double = 0.0,
                                                   isResizable: Boolean = true,
                                                   modality: Modality = Modality.NONE) {
            val stage = Stage()
            val loader = FXMLLoader(PasswordController::class.java.getResource(path))
            val parent = loader.load<Parent>()
            val controller = loader.getController<C>()
            controller.stage = stage
            controller.callback = callback
            stage.title = title
            stage.minWidth = minWidth
            stage.minHeight = minHeight
            stage.isResizable = isResizable
            stage.scene = Scene(parent)
            stage.initModality(modality)
            stage.initOwner(owner.scene.window)
            stage.show()
        }
    }
}