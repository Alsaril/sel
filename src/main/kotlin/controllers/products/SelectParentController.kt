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
        val result = api.productsData().await()
        if (!result.isSuccessful()) return@launch
        val items = result.notNullResult().nodes.map { TreeItem(it) }
        val roots = mutableListOf<TreeItem<Node>>()
        items.forEach {
            if (it.value.parent == null) {
                roots.add(it)
            } else {
                items[it.value.parent!! - 1].children.add(it)
            }
        }
        val root = TreeItem<Node>(Node("Все"))
        root.children.addAll(roots)
        nodeTreeView.root = root
    }


    companion object {
        fun show(owner: javafx.scene.Node, callback: CloseListener<Int>) {
            LoadController.show(owner, callback,
                    path = "/view/products/SelectParent.fxml",
                    title = "Выберете раздел",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}