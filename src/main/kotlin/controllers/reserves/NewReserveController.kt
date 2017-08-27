package controllers.reserves

import controllers.LoadController
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
import javafx.stage.Window
import models.operation.Position
import utils.CloseListener

class NewReserveController : LoadController<Boolean>() {
    @FXML private lateinit var positionTable: TableView<Position>
    @FXML private lateinit var nameColumn: TableColumn<Position, String>
    @FXML private lateinit var priceColumn: TableColumn<Position, String>
    @FXML private lateinit var countColumn: TableColumn<Position, String>
    @FXML private lateinit var unitColumn: TableColumn<Position, String>
    @FXML private lateinit var sumColumn: TableColumn<Position, String>

    @FXML private lateinit var totalSum: Label

    @FXML
    fun initialize() {
        nameColumn.setCellValueFactory(PropertyValueFactory("name"))
        priceColumn.setCellValueFactory(PropertyValueFactory("price"))
        countColumn.setCellValueFactory(PropertyValueFactory("count"))
        unitColumn.setCellValueFactory(PropertyValueFactory("unit"))
        sumColumn.setCellValueFactory(PropertyValueFactory("sum"))
    }

    fun addProduct() {

    }

    fun okHandle() {

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