package controllers.cashbox

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
import models.Product
import models.operation.Operation
import models.operation.Position
import start.Main
import utils.CloseListener

class OperationController : LoadController<Boolean>() {
    private val mainApp: Main? = null

    private var operationOL = FXCollections.observableArrayList<Operation>()
    private var productList: List<Product>? = null

    @FXML private lateinit var networkStatus: Label
    @FXML private lateinit var operationTable: TableView<Operation>
    @FXML private lateinit var typeColumn: TableColumn<Operation, String>
    @FXML private lateinit var dateColumn: TableColumn<Operation, String>
    @FXML private lateinit var userColumn: TableColumn<Operation, String>
    @FXML private lateinit var totalLable: Label

    @FXML private lateinit var userLabel: Label
    @FXML private lateinit var typeLabel: Label
    @FXML private lateinit var dateLabel: Label

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
        priceColumn.setCellValueFactory(PropertyValueFactory("twoPoints"))
        countColumn.setCellValueFactory(PropertyValueFactory("strCount"))
        discountColumn.setCellValueFactory(PropertyValueFactory("discountFormat"))
        sumColumn.setCellValueFactory(PropertyValueFactory("sumFormat"))
        unitColumn.setCellValueFactory(PropertyValueFactory("unit"))
        //---------------------------------------------------------------------------------------
        typeColumn.setCellValueFactory(PropertyValueFactory("typeString"))
        dateColumn.setCellValueFactory(PropertyValueFactory("dateFormat"))
        userColumn.setCellValueFactory(PropertyValueFactory("userName"))
        operationTable.selectionModel.selectedItemProperty().addListener { _, _, newValue -> showOperation(newValue) }
        loadOperationData()
        loadProducts()
        //---------------------------------------------------------------------------------------

        api.addStateListener {
            launch(JavaFx) {
                networkStatus.text = it.toString()
            }
        }
    }

    private fun loadOperationData() = launch(JavaFx) {
        val result = api.operations().await()
        if (result.isSuccessful()) {
            operationOL = FXCollections.observableArrayList(result.result)
            operationTable.setItems(operationOL)
        }
    }

    fun newOperation(actionEvent: ActionEvent) {
        NewOperationController.show(actionEvent.source as Node) { result ->
            if (result) {
                loadOperationData()
            }
        }
    }

    private fun showOperation(operation: Operation) {
        userLabel.text = operation.user
        typeLabel.text = operation.getTypeString()
        dateLabel.text = operation.getDateFormat()
        val positions = operation.positions
        var sum = 0.0
        positions.forEach { position ->
            getProductById(position.product)?.let {
                position.productName = it.name
                position.unit = it.unit
                position.isInteger = it.isInteger
                sum += position.sum()
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

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>? = null) {
            LoadController.show(owner, callback,
                    path = "/view/cashbox/OperationsView.fxml",
                    title = "Касса",
                    isResizable = true,
                    modality = Modality.NONE)
        }
    }
}
