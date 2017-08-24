package controllers.supply

import controllers.LoadController
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import utils.CloseListener

import java.io.IOException

/**
 * Created by andrey on 25.07.17.
 */
class SupplyViewController: LoadController<Boolean>(){
    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/supply/SuppliesView.fxml",
                    title = "Поставки",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
