package controllers.cashbox

import api.API
import api.APIMiddlewareImpl
import api.State
import api.StateListener
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
import javafx.stage.Stage
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Operation
import models.Position
import models.Product
import start.Main
import java.io.IOException

/**
 * Created by andrey on 24.07.17.
 */
class CashboxViewController {
    private val mainApp: Main? = null

    internal var api: API = APIMiddlewareImpl

    private var operationOL = FXCollections.observableArrayList<Operation>()
    private var operationList: List<Operation>? = null
    private var productList: List<Product>? = null

    @FXML private lateinit var networkStatus: Label
    @FXML private lateinit var operationTable: TableView<Operation>
    @FXML private lateinit var typeColumn: TableColumn<Operation, String>
    @FXML private lateinit var dateColumn: TableColumn<Operation, String>
    @FXML private lateinit var userColumn: TableColumn<Operation, String>
    @FXML private lateinit var totalLable: Label

    @FXML private lateinit var userLable: Label
    @FXML private lateinit var typeLable: Label
    @FXML private lateinit var dateLable: Label

    @FXML private lateinit var positionTable: TableView<Position>
    @FXML private lateinit var nameColumn: TableColumn<Position, String>
    @FXML private lateinit var priceColumn: TableColumn<Position, Float>
    @FXML private lateinit var unitColumn: TableColumn<Position, String>
    @FXML private lateinit var countColumn: TableColumn<Position, String>
    @FXML private lateinit var discountColumn: TableColumn<Position, String>
    @FXML private lateinit var sumColumn: TableColumn<Position, String>


    @FXML
    private fun initialize() {

        nameColumn.setCellValueFactory(PropertyValueFactory("productName"))
        priceColumn.setCellValueFactory(PropertyValueFactory("priceFormat"))
        countColumn.setCellValueFactory(PropertyValueFactory("countString"))
        discountColumn.setCellValueFactory(PropertyValueFactory("discountString"))
        sumColumn.setCellValueFactory(PropertyValueFactory("sumFormat"))
        unitColumn.setCellValueFactory(PropertyValueFactory("unit"))
        //---------------------------------------------------------------------------------------
        typeColumn.setCellValueFactory(PropertyValueFactory("typeString"))
        dateColumn.setCellValueFactory(PropertyValueFactory("dateFormat"))
        userColumn.setCellValueFactory(PropertyValueFactory("user"))
        operationTable.selectionModel.selectedItemProperty().addListener { _, _, newValue -> showOperation(newValue) }
        loadOperationData()
        loadProducts()
        //---------------------------------------------------------------------------------------

        api.addStateListener(object : StateListener {
            override fun stateChanged(state: State) {
                networkStatus.text = state.toString()
            }
        })
    }

    private fun loadOperationData() = launch(JavaFx) {
        val result = api.operations().await()
        if (result.isSuccessful()) {
            operationList = result.result
            operationOL = FXCollections.observableArrayList(result.result)
            operationTable.setItems(operationOL)
        }
    }

    fun newOperation(actionEvent: ActionEvent) {
        try {
            val stage = Stage()
            val loader = FXMLLoader()
            loader.location = javaClass.getResource("/view/cashbox/NewCashboxView.fxml")
            val categoryAddFXML = loader.load<Parent>()
            stage.title = "Кассовая операция"
            stage.minHeight = 600.0
            stage.minWidth = 800.0
            stage.isResizable = false
            stage.scene = Scene(categoryAddFXML)
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner((actionEvent.source as Node).scene.window)
            val controller = loader.getController<NewCashboxController>()
            stage.onCloseRequest = EventHandler { }
            stage.showAndWait()

            if (controller.isOkClicked) {
                loadOperationData()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun showOperation(operation: Operation) {
        userLable.text = operation.user
        typeLable.text = operation.typeString
        dateLable.text = operation.dateFormat
        val positions = operation.positions
        var sum = 0.0
        positions.forEach { position ->
            getProductById(position.product)?.let {
                position.productName = it.name
                setSum(position)
                it.unit = it.unit
                sum += position.sum
            }
        }
        val positionObservableList = FXCollections.observableArrayList(operation.positions)
        positionTable.items = positionObservableList
        totalLable.text = String.format("%.2f", sum)
    }

    private fun loadProducts() = launch(JavaFx) {
        val result = api.productsData().await()
        if (result.isSuccessful()) {
            productList = result.result?.products
        }
    }

    private fun getProductById(id: Int) = productList?.firstOrNull { it.id == id }

    private fun setSum(position: Position) {
        with(position) {
            val count = count
            val price = price
            val discount = discount

            if (count != null && price != null && discount != null) {
                sum = (price - discount) * count
            }
        }
    }
}
