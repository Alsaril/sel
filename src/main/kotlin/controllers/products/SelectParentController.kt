package controllers.products

import controllers.LoadController
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Node
import utils.CloseListener
import utils.createSortedTree

class SelectParentController : LoadController<Int>() {
    @FXML private lateinit var nodeTreeView: TreeView<Node>

    @FXML private fun initialize() {
        loadNodesData()
    }

    fun handleSelect() {
        val item = nodeTreeView.selectionModel.selectedItem.value
        close(item.id)
    }

    private fun loadNodesData() = launch(JavaFx) {
        val result = api.nodes().await()
        if (!result.isSuccessful()) return@launch
        val roots = createSortedTree(result.notNullResult())
        val root = TreeItem<Node>(Node("Все"))
        root.children.addAll(roots)
        nodeTreeView.root = root
    }


    companion object {
        fun show(owner: javafx.scene.Node, callback: CloseListener<Int>) {
            LoadController.show(owner, callback,
                    path = "/view/products/SelectParent.fxml",
                    title = "Выберите раздел",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}