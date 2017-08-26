package controllers.supply

import controllers.LoadController
import javafx.scene.Node
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import utils.CloseListener

/**
 * Created by andrey on 25.07.17.
 */
class SuppliesViewController : LoadController<Boolean>(){


    fun newSupply(){

    }
    fun showSupppliers(){

    }

    private fun loadSuppliesData() = launch(JavaFx) {
        val result = api.operations().await()
        if (result.isSuccessful()) {

        }
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/supply/SuppliesView.fxml",
                    title = "Поставки",
                    isResizable = true,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
