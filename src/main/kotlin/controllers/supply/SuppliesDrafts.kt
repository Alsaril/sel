package controllers.supply

import controllers.LoadController
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.supply.PositionSupplyFull
import models.supply.Supply
import utils.CloseListener
import utils.Dialogs
import utils.makeMenu

class SuppliesDrafts : LoadController<Boolean>() {

    @FXML private lateinit var suppliesTable: TableView<Supply>

    @FXML private lateinit var dateColumn: TableColumn<Supply, String>
    @FXML private lateinit var supplierColumn: TableColumn<Supply, String>
    @FXML private lateinit var documentColumn: TableColumn<Supply, String>

    @FXML private lateinit var productTable: TableView<PositionSupplyFull>

    @FXML private lateinit var productColumn: TableColumn<PositionSupplyFull, String>
    @FXML private lateinit var countColumn: TableColumn<PositionSupplyFull, String>
    @FXML private lateinit var priceColumn: TableColumn<PositionSupplyFull, String>


    @FXML private lateinit var documentLabel: Label
    @FXML private lateinit var infoDocumentLabel: Label
    @FXML private lateinit var dateDocumentLabel: Label
    @FXML private lateinit var dateAddLabel: Label



    @FXML
    private fun initialize() {
        dateColumn.setCellValueFactory(PropertyValueFactory("dateFormat"))
        supplierColumn.setCellValueFactory(PropertyValueFactory("supplierName"))
        documentColumn.setCellValueFactory(PropertyValueFactory("document"))
        suppliesTable.selectionModel.selectedItemProperty().addListener { _, _, newValue -> showSupply(newValue) }

        productColumn.setCellValueFactory(PropertyValueFactory("productName"))
        countColumn.setCellValueFactory(PropertyValueFactory("count"))
        priceColumn.setCellValueFactory(PropertyValueFactory("price"))

        makeMenu(suppliesTable) {
            "Редактировать" to { editDraft(it) }
            "Удалить" to { delDraft(it) }
        }

        loadSuppliesData()
    }

    fun newSupply(actionEvent: ActionEvent) {
        NewSupplyController.show(owner = actionEvent.source as Node) { result ->
            if (result) {
                loadSuppliesData()
            }
        }

    }

    private fun editDraft(supply: Supply) {
        NewSupplyController.show(owner = documentLabel, supply = supply) { result ->
            if (result) {
                loadSuppliesData()
            }
        }

    }

    private fun delDraft(supply: Supply) {}

    fun showSuppliers(actionEvent: ActionEvent) {
        SuppliersViewController.show(actionEvent.source as Node) {}
    }

    private fun showSupply(supply: Supply?) {
        if (supply != null) {
            documentLabel.text = supply.document
            infoDocumentLabel.text = supply.documentInfo
            dateDocumentLabel.text = supply.documentDate
            dateAddLabel.text = supply.getDateFormat()
            productTable.items.setAll(supply.positions)
        } else {
            documentLabel.text = "Нет данных"
            infoDocumentLabel.text = "Нет данных"
            dateDocumentLabel.text = "Нет данных"
            dateAddLabel.text = "Нет данных"
            productTable.items = FXCollections.observableArrayList()
        }
    }

    private fun loadSuppliesData() = launch(JavaFx) {
        val result = api.drafts().await()
        if (result.isSuccessful()) {
            suppliesTable.items.setAll(result.result)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>? = null) {
            LoadController.show(owner, callback,
                    path = "/view/supply/SuppliesDrafts.fxml",
                    title = "Черновики поставок",
                    isResizable = true,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
