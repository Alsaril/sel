package controllers.reserves

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
import javafx.stage.Window
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Product
import models.reserve.Client
import models.reserve.ReserveMin
import models.reserve.ReservePositionFull
import utils.CloseListener
import utils.Dialogs
import utils.parseDouble
import java.text.SimpleDateFormat
import java.util.*

class NewReserveController : LoadController<Boolean>() {
    private var positionsOL: ObservableList<ReservePositionFull> = FXCollections.observableArrayList()

    @FXML private lateinit var positionTable: TableView<ReservePositionFull>
    @FXML private lateinit var nameColumn: TableColumn<ReservePositionFull, String>
    @FXML private lateinit var priceColumn: TableColumn<ReservePositionFull, String>
    @FXML private lateinit var countColumn: TableColumn<ReservePositionFull, String>
    @FXML private lateinit var unitColumn: TableColumn<ReservePositionFull, String>
    @FXML private lateinit var sumColumn: TableColumn<ReservePositionFull, String>

    @FXML private lateinit var totalSum: Label
    @FXML private lateinit var comment: TextArea
    @FXML private lateinit var clients: ComboBox<Client>

    @FXML
    fun initialize() {
        positionTable.isEditable = true

        nameColumn.setCellValueFactory(PropertyValueFactory("name"))
        priceColumn.cellValueFactory = PropertyValueFactory("twoPoints")

        priceColumn.cellFactory = TextFieldTableCell.forTableColumn()
        priceColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as ReservePositionFull
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
            val position = t.tableView.items[t.tablePosition.row] as ReservePositionFull
            val count = parseDouble(t.newValue)
            if (count == null) {
                Dialogs.showErrorDialog("Введено нечисловое значение")
                return@EventHandler
            }
            position.count = count
            refresh()
        }
        unitColumn.setCellValueFactory(PropertyValueFactory("unit"))
        sumColumn.setCellValueFactory(PropertyValueFactory("sumFormat"))

        showPositionList()
        refresh()
        loadClients()
    }

    private fun loadClients() = launch(JavaFx) {
        val result = api.clients().await()
        if (result.isSuccessful()) {
            clients.items = FXCollections.observableArrayList(result.notNullResult())
        } else {
            Dialogs.showErrorDialog("Ошибка при загрузке клиентов: " + result.error)
        }
    }

    fun addProduct(actionEvent: ActionEvent) {
        ProductViewController.show(select = true, owner = actionEvent.source as Node) { result ->
            newPosition(result)
        }
    }

    private fun newPosition(product: Product?) {
        if (product != null) {
            val position = ReservePositionFull(
                    count = 1.0,
                    price = product.price,
                    product = product)
            positionsOL.addAll(position)
            refresh()
        }
    }

    fun okHandle() {
        val simpleDateFormat = SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())

        val client: Client? = clients.selectionModel.selectedItem
        if (client == null) {
            Dialogs.showErrorDialog("Не выбран клиент")
            return
        }
        val reserve = ReserveMin(0, "", simpleDateFormat.format(Date()),
                comment.text, client.id, positionsOL.map { it.toMin() })

        addReserve(reserve)
    }

    private fun addReserve(reserve: ReserveMin) = launch(JavaFx) {
        val result = api.addReserve(reserve).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Резерв добавлен успешно!")
            close(true)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
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
        fun show(owner: Window, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/reserves/NewReserve.fxml",
                    title = "Новый резерв",
                    minHeight = 600.0,
                    minWidth = 800.0,
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}