package controllers

import api.API
import api.APIMiddlewareImpl
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import utils.CloseListener

abstract class LoadController<T> {

    protected var api: API = APIMiddlewareImpl

    protected lateinit var stage: Stage
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
                                                   modality: Modality = Modality.NONE,
                                                   init: (C.() -> Unit)? = null) {
            Companion.show<T, C>(owner.scene.window, callback, path, title, minWidth, minHeight, isResizable, modality, init)
        }

        public fun <T, C : LoadController<T>> show(owner: Window, callback: CloseListener<T>,
                                                   path: String,
                                                   title: String = "",
                                                   minWidth: Double = 0.0,
                                                   minHeight: Double = 0.0,
                                                   isResizable: Boolean = true,
                                                   modality: Modality = Modality.NONE,
                                                   init: (C.() -> Unit)? = null) {
            val stage = Stage()
            val loader = FXMLLoader(LoadController::class.java.getResource(path))
            val parent = loader.load<Parent>()
            val controller = loader.getController<C>()
            controller.stage = stage
            controller.callback = callback
            init?.invoke(controller)
            stage.title = title
            stage.minWidth = minWidth
            stage.minHeight = minHeight
            stage.isResizable = isResizable
            stage.scene = Scene(parent)
            stage.initModality(modality)
            stage.initOwner(owner)
            stage.show()
        }
    }
}