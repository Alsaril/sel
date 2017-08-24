package controllers.cashbox

import controllers.LoadController
import controllers.products.ProductViewController
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.stage.Modality
import javafx.stage.Stage
import models.Operation
import models.Position
import models.Product
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.CloseListener
import utils.Dialogs
import java.text.SimpleDateFormat
import java.util.*

class NewOperationController : LoadController<Boolean>() {

    private var api = RetrofitClient.getApiService()

    private var positionsOL: ObservableList<Position>? = FXCollections.observableArrayList()
    private var positionList: List<Position>? = null

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
        priceColumn.cellValueFactory = PropertyValueFactory("priceFormat")

        priceColumn.cellFactory = TextFieldTableCell.forTableColumn()
        priceColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as Position
            val price = java.lang.Double.parseDouble(t.newValue)
            position.price = price
            refresh()
        }

        countColumn.cellValueFactory = PropertyValueFactory("countString")
        countColumn.cellFactory = TextFieldTableCell.forTableColumn()
        countColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as Position
            val count = java.lang.Double.parseDouble(t.newValue)
            position.count = count
            refresh()
        }
        discountColumn.cellValueFactory = PropertyValueFactory("discountString")
        discountColumn.cellFactory = TextFieldTableCell.forTableColumn()
        discountColumn.onEditCommit = EventHandler { t ->
            val position = t.tableView.items[t.tablePosition.row] as Position
            val discount = java.lang.Double.parseDouble(t.newValue)
            position.discount = discount
            refresh()
        }
        sumColumn.cellValueFactory = PropertyValueFactory("sumFormat")
        unitColumn.cellValueFactory = PropertyValueFactory("unit")
        showPositionList()
        refresh()
    }

    fun addProduct(actionEvent: ActionEvent) {
        val stage = Stage()
        val loader = FXMLLoader()
        loader.location = javaClass.getResource("/view/cashbox/SelectProductView.fxml")
        val FXML = loader.load<Parent>()
        stage.title = "Выбор товара"
        stage.isResizable = false
        stage.scene = Scene(FXML)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.initOwner((actionEvent.source as Node).scene.window)

        val controller = loader.getController<ProductViewController>()
        stage.showAndWait()

        val product = controller.selectProduct
        if (product != null) {
            newPosition(product)
        }
    }

    private fun newPosition(product: Product) {
        val position = Position(
                count = 1.0,
                price = java.lang.Double.parseDouble(product.price.toString()),
                product = product.id)

        positionsOL?.addAll(position)
        refresh()
    }

    fun okHandle() {
        val simpleDateFormat = SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val operation = Operation(
                date = simpleDateFormat.format(Date()),
                type = if (returnCheckBox.isSelected) 1 else 0,
                positions = positionsOL ?: listOf())

        addOperation(operation)
    }

    fun printHandle() {

    }

    private fun addOperation(operation: Operation) {
        val call = api.addOperation(operation)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 201) {
                    Dialogs.showDialog("Операция проведена успешно!")
                    Platform.runLater { close(true) }
                } else {
                    Dialogs.showExeptionDialog(response.message())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Dialogs.showExeptionDialog(t.message ?: "Unknown error")
            }
        })

    }

    private fun refresh() {
        positionTable.refresh()
        var sum = 0.0
        positionsOL?.forEach { sum += it.sum() }
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
