package controllers.cashbox

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
import models.operation.Operation
import models.operation.Position
import utils.*
import java.text.SimpleDateFormat
import java.util.*

class NewOperationController : LoadController<Boolean>() {

    private var positionsOL: ObservableList<Position> = FXCollections.observableArrayList()

    @FXML private lateinit var returnCheckBox: CheckBox
    @FXML private lateinit var totalSum: Label
    @FXML private lateinit var positionTable: TableView<Position>
    @FXML private lateinit var nameColumn: TableColumn<Position, String>
    @FXML private lateinit var priceColumn: TableColumn<Position, String>
    @FXML private lateinit var unitColumn: TableColumn<Position, String>
    @FXML private lateinit var countColumn: TableColumn<Position, String>
    @FXML private lateinit var discountColumn: TableColumn<Position, String>
    @FXML private lateinit var sumColumn: TableColumn<Position, String>


    @FXML
    private fun initialize() {
        positionTable.isEditable = true
        nameColumn.cellValueFactory = PropertyValueFactory("productName")
        priceColumn.cellValueFactory = PropertyValueFactory("twoPoints")

        priceColumn.cellFactory = TextFieldTableCell.forTableColumn()
        priceColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as Position
            val price = parseDouble(t.newValue)
            if (price == null) {
                Dialogs.showErrorDialog("Введено нечисловое значение")
                return@EventHandler
            }
            position.price = price
            refresh()
        }

        countColumn.cellValueFactory = PropertyValueFactory("strCount")
        countColumn.cellFactory = TextFieldTableCell.forTableColumn()
        countColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as Position
            val count = parseDouble(t.newValue)
            if (count == null) {
                Dialogs.showErrorDialog("Введено нечисловое значение")
                return@EventHandler
            }
            position.count = count
            refresh()
        }
        discountColumn.cellValueFactory = PropertyValueFactory("discountFormat")
        discountColumn.cellFactory = TextFieldTableCell.forTableColumn()
        discountColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as Position
            val discount = parseDouble(t.newValue)
            if (discount == null) {
                Dialogs.showErrorDialog("Введено нечисловое значение")
                return@EventHandler
            }
            position.discount = discount
            refresh()
        }
        sumColumn.cellValueFactory = PropertyValueFactory("sumFormat")
        unitColumn.cellValueFactory = PropertyValueFactory("unit")

        val contextMenu = ContextMenu()
        val deletePosition = MenuItem("Удалить")
        deletePosition.onAction = EventHandler {
            val item = positionTable.selectionModel.selectedItem
            delPosition(item)
        }

        contextMenu.items.setAll(deletePosition)
        positionTable.contextMenu = contextMenu

        showPositionList()
        refresh()


    }

    fun addProduct(actionEvent: ActionEvent) {
        ProductViewController.show(select = true, owner = actionEvent.source as Node) { result ->
            newPosition(result)
        }
    }

    private fun newPosition(product: Product?) {
        if (product != null) {
            val position = Position(
                    count = 1.0,
                    price = product.price,
                    product = product.id)
            position.productName = product.name
            position.unit = product.unit
            position.isInteger = product.isInteger
            position.discount = 0.0

            positionsOL.addAll(position)
            refresh()
        }
    }

    fun okHandle() {
        val simpleDateFormat = SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val operation = Operation(
                date = simpleDateFormat.format(Date()),
                type = if (returnCheckBox.isSelected) 1 else 0,
                positions = positionsOL)

        addOperation(operation)
    }

    fun printHandle() {
        Printer.printCheck(
                0,
                "2341234320099",
                Utils.cashboxDate(),
                2312,
                Utils.cashboxTime(),
                positionsOL.map {
                    utils.Position("",
                            it.productName,
                            it.price.twoPoints(),
                            if (it.isInteger) it.count.noPoints() else it.count.twoPoints(),
                            it.sum().twoPoints())
                },
                totalSum.text,
                totalSum.text,
                "0,00",
                "Человек"
        )
    }

    private fun addOperation(operation: Operation) = launch(JavaFx) {
        val result = api.addOperation(operation).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Операция проведена успешно!")
            close(true)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    private fun delPosition(position: Position) {
        positionsOL.remove(position)
    }

    private fun refresh() {
        positionTable.refresh()
        var sum = 0.0
        positionsOL.forEach { sum += it.sum() }
        totalSum.text = String.format("%.2f", sum)
    }

    private fun showPositionList() {
        positionTable.items = positionsOL
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/cashbox/NewCashboxView.fxml",
                    title = "Кассовая операция",
                    minHeight = 600.0,
                    minWidth = 800.0,
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
