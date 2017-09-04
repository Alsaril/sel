package controllers.supply

import controllers.LoadController
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.supply.Supplier
import utils.CloseListener
import utils.Dialogs


/**
 * Created by andrey on 25.07.17.
 */
class SuppliersViewController : LoadController<Boolean>() {

    @FXML private lateinit var suppliersTable: TableView<Supplier>

    @FXML private lateinit var nameColumn: TableColumn<Supplier, String>

    @FXML private lateinit var name: Label
    @FXML private lateinit var inn: Label
    @FXML private lateinit var type: Label
    @FXML private lateinit var urAddress: Label
    @FXML private lateinit var physAddress: Label
    @FXML private lateinit var requisites: Label
    @FXML private lateinit var name1: Label
    @FXML private lateinit var phone1: Label
    @FXML private lateinit var name2: Label
    @FXML private lateinit var phone2: Label

    @FXML
    private fun initialize() {
        nameColumn.setCellValueFactory(PropertyValueFactory("name"))
        suppliersTable.selectionModel.selectedItemProperty().addListener { _, _, newValue -> newValue?.let { showSupplier(newValue) } }

        loadSuppliersData()

        val contextMenu = ContextMenu()
        val edit = MenuItem("Редактировать")
        edit.onAction = EventHandler {
            val item = suppliersTable.selectionModel.selectedItem
            editSupplier(item)
        }
        contextMenu.items.setAll(edit)
        suppliersTable.contextMenu = contextMenu
    }

    fun handleAdd(actionEvent: ActionEvent) {
        SupplierEditController.show(owner = actionEvent.source as Node) { result ->
            if (result) {
                loadSuppliersData()
            }
        }
    }

    fun editSupplier(supplier: Supplier) {
        SupplierEditController.show(owner = name as Node, supplier = supplier) { result ->
            if (result) {
                loadSuppliersData()
            }
        }
    }

    fun showSupplier(supplier: Supplier) {
        name.text = supplier.name
        inn.text = supplier.inn
        // нужен метод который преводит тип в строку
        //type.text = supplier.type
        urAddress.text = supplier.urAddress
        physAddress.text = supplier.physAddress
        requisites.text = supplier.requisites
        name1.text = supplier.name1
        phone1.text = supplier.phone1
        name2.text = supplier.name2
        phone2.text = supplier.phone2
    }

    private fun loadSuppliersData() = launch(JavaFx) {
        val result = api.suppliers().await()
        if (result.isSuccessful()) {
            val suppliers = result.notNullResult()
            suppliersTable.items = FXCollections.observableArrayList(suppliers)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/supply/SuppliersView.fxml",
                    title = "Поставщики",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
