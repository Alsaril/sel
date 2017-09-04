package controllers.supply

import controllers.LoadController
import controllers.products.ProductViewController
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Product
import models.supply.PositionSupplyFull
import models.supply.Supplier
import models.supply.SupplyMin
import utils.CloseListener
import utils.Dialogs
import utils.Utils
import utils.parseDouble

/**
 * Created by andrey on 25.07.17.
 */
class NewSupplyController : LoadController<Boolean>() {

    private var positionsOL: ObservableList<PositionSupplyFull> = FXCollections.observableArrayList()

    @FXML private lateinit var suppliers: ComboBox<Supplier>
    @FXML private lateinit var document: TextField
    @FXML private lateinit var documentInfo: TextArea
    @FXML private lateinit var documentDate: DatePicker


    @FXML private lateinit var positionTable: TableView<PositionSupplyFull>
    @FXML private lateinit var nameColumn: TableColumn<PositionSupplyFull, String>
    @FXML private lateinit var vendorColumn: TableColumn<PositionSupplyFull, String>
    @FXML private lateinit var priceColumn: TableColumn<PositionSupplyFull, String>
    @FXML private lateinit var unitPriceColumn: TableColumn<PositionSupplyFull, String>
    @FXML private lateinit var unitColumn: TableColumn<PositionSupplyFull, String>
    @FXML private lateinit var countColumn: TableColumn<PositionSupplyFull, String>
    @FXML private lateinit var sellPriceColumn: TableColumn<PositionSupplyFull, String>


    @FXML
    private fun initialize() {
        positionTable.isEditable = true
        nameColumn.cellValueFactory = PropertyValueFactory("productName")
        vendorColumn.cellValueFactory = PropertyValueFactory("productVendor")

        priceColumn.cellValueFactory = PropertyValueFactory("fullPriceFormat")
        priceColumn.cellFactory = TextFieldTableCell.forTableColumn()
        priceColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as PositionSupplyFull
            val price = parseDouble(t.newValue)
            if (price == null) {
                Dialogs.showErrorDialog("Введено нечисловое значение")
                return@EventHandler
            }
            position.fullPrice = price
            position.price = price / position.count
            refresh()
        }
        countColumn.cellValueFactory = PropertyValueFactory("strCount")
        countColumn.cellFactory = TextFieldTableCell.forTableColumn()
        countColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as PositionSupplyFull
            val count = parseDouble(t.newValue)
            if (count == null) {
                Dialogs.showErrorDialog("Введено нечисловое значение")
                return@EventHandler
            }
            position.count = count
            position.price = position.fullPrice / position.count
            refresh()
        }
        sellPriceColumn.cellValueFactory = PropertyValueFactory("sellPriceFormat")
        sellPriceColumn.cellFactory = TextFieldTableCell.forTableColumn()
        sellPriceColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as PositionSupplyFull
            val sellPrice = parseDouble(t.newValue)
            if (sellPrice == null) {
                Dialogs.showErrorDialog("Введено нечисловое значение")
                return@EventHandler
            }
            position.sellPrice = sellPrice
            refresh()
        }
        unitColumn.cellValueFactory = PropertyValueFactory("productUnit")
        unitPriceColumn.cellValueFactory = PropertyValueFactory("priceFormat")


        val contextMenu = ContextMenu()
        val calc = MenuItem("Посчитать цену")
        calc.onAction = EventHandler {
            val item = positionTable.selectionModel.selectedItem
            calcSellPrice(item)
        }
        contextMenu.items.setAll(calc)
        positionTable.contextMenu = contextMenu

        loadSuppliers()

        showPositionList()
        refresh()
    }

    fun addPosition(actionEvent: ActionEvent) {
        ProductViewController.show(select = true, owner = actionEvent.source as Node) { result ->
            newPosition(result)
        }
    }

    private fun newPosition(product: Product?) {
        if (product != null) {
            val position = PositionSupplyFull(
                    count = 1.0,
                    price = 0.0,
                    sellPrice = product.price,
                    product = product)
            positionsOL.addAll(position)
            refresh()
        }
    }

    fun handleOk() {
        val supply = SupplyMin()
        supply.supplier = suppliers.selectionModel.selectedItem.id
        supply.document = Utils.fieldCheck(document.text)
        supply.documentInfo = Utils.fieldCheck(documentInfo.text)
        if (documentDate.value != null) {
            supply.documentDate = Utils.fieldCheck(documentDate.value.toString())
        } else {
            supply.documentDate = "нет данных"
        }

        supply.positions = positionsOL.map { it.toMin() }
        addSupply(supply)
    }

    fun handleSave() {
        val supply = SupplyMin()
        supply.supplier = suppliers.selectionModel.selectedItem.id
        supply.document = Utils.fieldCheck(document.text)
        supply.documentInfo = Utils.fieldCheck(documentInfo.text)
        if (documentDate.value != null) {
            supply.documentDate = Utils.fieldCheck(documentDate.value.toString())
        } else {
            supply.documentDate = "нет данных"
        }

        supply.positions = positionsOL.map { it.toMin() }
        addDraft(supply)
    }

    fun printHandle() {

    }

    private fun addDraft(supplyMin: SupplyMin) = launch(JavaFx) {
        val result = api.addDraft(supplyMin).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Сохранено!")
            close(true)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    private fun addSupply(supplyMin: SupplyMin) = launch(JavaFx) {
        val result = api.addSupply(supplyMin).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Документ провден!")
            close(true)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    private fun delDraft(supplyMin: SupplyMin) = launch(JavaFx) {
        val result = api.delDraft(supplyMin.id.toString()).await()
        if (result.isSuccessful()) {
        } else {
            Dialogs.showExeptionDialog("del draft: " + result.error)
        }
    }

    private fun refresh() {
        positionTable.refresh()
    }

    private fun showPositionList() {
        positionTable.items = positionsOL
    }

    fun loadSuppliers() = launch(JavaFx) {
        val result = api.suppliers().await()
        if (result.isSuccessful()) {
            suppliers.items = FXCollections.observableArrayList(result.result)
        }
    }

    private fun calcSellPrice(position: PositionSupplyFull) {
        val dialog = TextInputDialog()
        dialog.title = "Цена"
        dialog.headerText = "Накинуть сверху:"
        dialog.contentText = "%:"
        val result = dialog.showAndWait()

        result.ifPresent({ p ->
            val d = parseDouble(p)
            if (d != null) {
                position.sellPrice = position.price + (position.price * (d / 100))
            }
        })
        refresh()
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/supply/NewSupply.fxml",
                    title = "Новая поставка",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
